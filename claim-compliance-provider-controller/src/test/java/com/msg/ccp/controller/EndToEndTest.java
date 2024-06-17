package com.msg.ccp.controller;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.msg.ccp.claims.ClaimsCredentialsService;
import com.msg.ccp.wiremock.InjectWireMock;
import com.msg.ccp.wiremock.WireMockTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(WireMockTestResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class EndToEndTest {
    @InjectWireMock
    WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    private int vcId = 0;

    @Test
    @TestHTTPEndpoint(ClaimComplianceProviderController.class)
    void testSendClaimsEndpoint() throws IOException, URISyntaxException {
        // prepare
        final ObjectMapper objectMapper = new ObjectMapper();

        final Map<String, Object> claimsAndCredentials = readClaimsAndCredentials(objectMapper);
        @SuppressWarnings("unchecked") final List<Map<String, Object>> credentialsFromPayload = (List<Map<String, Object>>) claimsAndCredentials.get("verifiableCredentials");
        @SuppressWarnings("unchecked") final List<Map<String, Object>> claimsFromPayload = (List<Map<String, Object>>) claimsAndCredentials.get("claims");
        final Set<Map<String, Object>> allVerifiableCredentials = new HashSet<>(credentialsFromPayload);

        for (final Map<String, Object> claim : claimsFromPayload) {
            final String jsonRequest = objectMapper.writeValueAsString(claim);
            final Map<String, Object> vc = wrapWithVC(claim);
            final String jsonResponse = objectMapper.writeValueAsString(vc);
            allVerifiableCredentials.add(vc);
            stubFor(post(urlEqualTo("/vc-from-claims"))
                    .withRequestBody(equalToJson(jsonRequest, true, false))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(jsonResponse)));
        }

        final Set<Map<String, Object>> credentialsForComplianceService  = prepareCredentialsForComplianceService(allVerifiableCredentials);
        final Map<String, Object> sdCreatorResponseVpWithoutProof = this.wrapWithVPWithoutProof(credentialsForComplianceService);
        stubFor(post(urlEqualTo("/vp-without-proof-from-vcs"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(credentialsForComplianceService), true, false))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(sdCreatorResponseVpWithoutProof))));

        final Map<String, Object> complianceServiceResponse = createComplianceServiceResponse(sdCreatorResponseVpWithoutProof);
        stubFor(post(urlEqualTo("/api/credential-offers"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(sdCreatorResponseVpWithoutProof), true, false))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(complianceServiceResponse))));
        allVerifiableCredentials.add(complianceServiceResponse);

        final Map<String, Object> sdCreatorResponseVpWithProof = wrapWithVPWithProof(allVerifiableCredentials);
        stubFor(post(urlEqualTo("/vp-from-vcs"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(allVerifiableCredentials), true, false))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(sdCreatorResponseVpWithProof))));

        stubFor(post(urlEqualTo("/verification"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(sdCreatorResponseVpWithProof), true, false))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("")));

        // action
        final Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(claimsAndCredentials)
                        .when()
                        .post("/send-claims");

        // test
        final VerifiablePresentation resultVP = response.getBody().as(VerifiablePresentation.class);
        assertThat(response.getStatusCode()).isEqualTo(200);
        // TODO test if response payload is as expected
        assertThat(resultVP.getLdProof()).isNotNull();
        assertThat(resultVP.getLdProof().getType()).isEqualTo("JsonWebSignature2020");
        @SuppressWarnings("unchecked") final List<Map<String, Object>> vcs = (List<Map<String, Object>>) resultVP.getJsonObject().get("verifiableCredential");
        final Map<String, VerifiableCredential> id2vcMap = vcs.stream().collect(Collectors.toMap(vc -> (String) vc.get("id"), VerifiableCredential::fromMap));
        assertThat(id2vcMap).hasSize(10)
                .containsKey("https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json")
                .containsKey("https://participant.gxfs.gx4fm.org/.well-known/legalRegistrationNumber.json")
                .containsKey("https://gaia-x.eu/.well-known/complianceCredentialId")
                .containsKey("https://participant.gxfs.gx4fm.org/.well-known/termsAndConditions.json");
        assertThat(id2vcMap.get("https://participant.gxfs.gx4fm.org/.well-known/legalParticipant.json").getLdProof()).isNotNull();
        assertThat(id2vcMap.get("https://participant.gxfs.gx4fm.org/.well-known/legalRegistrationNumber.json").getLdProof()).isNotNull();
        assertThat(id2vcMap.get("https://gaia-x.eu/.well-known/complianceCredentialId").getLdProof()).isNotNull();
        assertThat(id2vcMap.get("https://participant.gxfs.gx4fm.org/.well-known/termsAndConditions.json").getLdProof()).isNotNull();
        assertThat(id2vcMap.get("https://participant.gxfs.gx4fm.org/.well-known/termsAndConditions.json").getLdProof().getVerificationMethod()).isEqualTo(new URI("did:web:participant.gxfs.gx4fm.org#JWK2020-RSA"));
        assertThat(id2vcMap.keySet().stream().filter(k -> k.startsWith("https://gaia-x.eu/.well-known/vcId/")).count()).isEqualTo(6);
    }

    private Map<String, Object> readClaimsAndCredentials(final ObjectMapper objectMapper) throws IOException {
        final String inputPayload = Files.readString(Path.of("src/test/resources/end2end/01_serviceInput.json"));
        return objectMapper.readValue(inputPayload, new TypeReference<>() {});
    }

    @SuppressWarnings("unchecked") // unchecked casts from Object to Map<String, Object>
    private Set<Map<String, Object>> prepareCredentialsForComplianceService(final Set<Map<String, Object>> allVerifiableCredentialMaps) {
        // filter only have gaja-x specific credentials or VCs and skip the domain specific ones (here: surveyonto:SurveyResultDataOffering)
        return allVerifiableCredentialMaps.stream()
                .filter(
                        vc -> ClaimsCredentialsService.GX_TYPES.stream().anyMatch(
                                type -> isGaiaXType((Map<String, Object>) vc.get("credentialSubject"), type)))
                .collect(Collectors.toSet());
    }

    private Map<String, Object> createComplianceServiceResponse(final Map<String, Object> sdCreatorResponseVP) {
        final Map<String, Object> result = new HashMap<>();
        result.put("@context", new HashSet<>() {
            {
                add("https://www.w3.org/2018/credentials/v1");
                add("https://w3id.org/security/suites/jws-2020/v1");
                add("https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#");
            }
        });
        result.put("id", "https://gaia-x.eu/.well-known/complianceCredentialId");
        result.put("type", "VerifiableCredential");
        result.put("issuer", "did:web:compliance.lab.gaia-x.eu:development");
        result.put("issuanceDate", "2024-05-15T00:00:00Z");
        result.put("expirationDate", "2034-05-15T00:00:00Z");
        result.put("credentialSubject", createComplianceCredentials(sdCreatorResponseVP));
        result.put("proof", new HashMap<>() {
            {
                put("type", "JsonWebSignature2020");
                put("created", "2024-05-03T13:30:48Z");
                put("proofPurpose", "assertionMethod");
                put("verificationMethod", "did:web:participant.gxfs.gx4fm.org#JWK2020-RSA");
                put("jws", "eyJhbGciOiJFZERTQSIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..XJ9");
            }
        });
        return result;
    }

    @SuppressWarnings("unchecked") // unchecked casts from Object to Set<Map<String, Object>> or Map<String, Object>
    private Set<Map<String, Object>> createComplianceCredentials(final Map<String, Object> sdCreatorResponseVP) {
        final Set<Map<String, Object>> result = new HashSet<>();
        final Set<Map<String, Object>> verifiableCredentials = (Set<Map<String, Object>>) sdCreatorResponseVP.get("verifiableCredential");
        for (final Map<String, Object> vc : verifiableCredentials) {
            final Map<String, Object> complianceCredential = new HashMap<>();
            complianceCredential.put("id", vc.get("id"));
            complianceCredential.put("type", "gx:compliance");
            complianceCredential.put("gx:integrity", "sha256-69c54048c41cc42019c5609a09814b0d06eb19c00f4c5f239cee663ad7207e79");
            complianceCredential.put("gx:integrityNormalization", "RFC8785:JCS");
            complianceCredential.put("gx:version", "22.10");
            complianceCredential.put("gx:type", retrieveType((Map<String, Object>)vc.get("credentialSubject")));
            result.add(complianceCredential);
        }
        return result;
    }

    private Object retrieveType(final Map<String, Object> credentialSubject) {
        final String typeKey = credentialSubject.containsKey("type") ? "type" : "@type";
        return credentialSubject.get(typeKey);
    }

    private boolean isGaiaXType(final Map<String, Object> credentialSubject, final String gaiaXTypeName) {
        return containsType(credentialSubject.get("type"), gaiaXTypeName) || containsType(credentialSubject.get("@type"), gaiaXTypeName);
    }

    private boolean containsType(final Object typeObject, final String typeName) {
        if (typeObject instanceof Collection) {
            return ((Collection<?>) typeObject).stream().map(Object::toString).anyMatch(type -> type.contains(typeName));
        } else if (typeObject instanceof String) {
            return ((String) typeObject).contains(typeName);
        }
        return false;
    }

    private Map<String, Object> wrapWithVC(final Map<String, Object> stringObjectMap) {
        return new HashMap<>() {
            {
                put("id", "https://gaia-x.eu/.well-known/vcId/" + vcId++);
                put("type", "VerifiableCredential");
                put("issuer", "https://gaia-x.eu/.well-known/unitTest.json");
                put("issuanceDate", "2024-05-15T00:00:00Z");
                put("credentialSubject", stringObjectMap);
            }
        };
    }

    private Map<String, Object> wrapWithVPWithoutProof(final Set<Map<String, Object>> credentials) {
        return new HashMap<>() {
            {
                put("id", "https://gaia-x.eu/.well-known/vpId");
                put("type", "VerifiablePresentation");
                put("issuer", "https://gaia-x.eu/.well-known/unitTest.json");
                put("issuanceDate", "2024-05-15T00:00:00Z");
                put("verifiableCredential", credentials);
            }
        };
    }

    private Map<String, Object> wrapWithVPWithProof(final Set<Map<String, Object>> credentials) {
        final Map<String, Object> result = wrapWithVPWithoutProof(credentials);
        result.put("proof", new HashMap<>() {
            {
                put("type", "JsonWebSignature2020");
                put("created", "2024-05-03T13:30:48Z");
                put("proofPurpose", "assertionMethod");
                put("verificationMethod", "did:web:participant.gxfs.gx4fm.org#JWK2020-RSA");
                put("jws", "eyJhbGciOiJFZERTQSIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..XJ9");
            }
        });
        return result;
    }
}