package com.msg.ccp.controller;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.msg.ccp.catalogue.FederatedCatalogueService;
import com.msg.ccp.claims.ClaimsCredentialsService;
import com.msg.ccp.compliance.ComplianceServiceService;
import com.msg.ccp.controller.payload.GenerateClaimsPayload;
import com.msg.ccp.sdcreator.SdCreatorService;
import com.msg.ccp.util.VpVcUtil;
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

    private final String SERVICE_OFFERING_TYPE = "gx:ServiceOffering";
    private final List<String> NON_SERVICE_OFFERING_TYPES = Arrays.asList("gx:ServiceOffering", "gx:LegalParticipant", "gx:legalRegistrationNumber", "gx:GaiaXTermsAndConditions");

    @BeforeEach
    public void setup() {
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    private int vcId = 0;

    @Test
    @TestHTTPEndpoint(ClaimComplianceProviderController.class)
    void testSendClaimsEndpoint() throws IOException {
        // prepare
        final ObjectMapper objectMapper = new ObjectMapper();

        final Map<String, Object> claimsAndCredentials = readClaimsAndCredentials(objectMapper);
        @SuppressWarnings("unchecked") final List<Map<String, Object>> credentialsFromPayload = (List<Map<String, Object>>) claimsAndCredentials.get("verifiableCredentials");
        @SuppressWarnings("unchecked") final List<Map<String, Object>> claimsFromPayload = (List<Map<String, Object>>) claimsAndCredentials.get("claims");
        final Set<Map<String, Object>> allVerifiableCredentials = new HashSet<>(credentialsFromPayload);
        final Set<Map<String, Object>> verifiableCredentialsServiceOffering = new HashSet<>();
        final Set<Map<String, Object>> verifiableCredentialsResourceOffering = new HashSet<>();

        for (final Map<String, Object> claim : claimsFromPayload) {
            final String jsonRequest = objectMapper.writeValueAsString(claim);
            final Map<String, Object> vc = wrapWithVC(claim);
            final String jsonResponse = objectMapper.writeValueAsString(vc);
            final String type = retrieveType(claim);
            allVerifiableCredentials.add(vc);
            // Add to allVerifiableCredentialsServiceOffering if type is SERVICE_OFFERING_TYPE
            if (SERVICE_OFFERING_TYPE.equals(type)) {
                verifiableCredentialsServiceOffering.add(vc);
            }
            // Add to allVerifiableCredentialsResourceOffering if type is not in NON_SERVICE_OFFERING_TYPES
            if (type != null && !NON_SERVICE_OFFERING_TYPES.contains(type)) {
                verifiableCredentialsResourceOffering.add(vc);
            }
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

        final Map<String, Object> serviceOfferingVpWithProof = wrapWithVPWithProof(verifiableCredentialsServiceOffering);
        final Map<String, Object> resourceOfferingVpWithProof = wrapWithVPWithProof(verifiableCredentialsResourceOffering);
        final Map<String, Object> complianceCredentialVpWithProof = wrapWithVPWithProof(Set.of(complianceServiceResponse));
        stubFor(post(urlEqualTo("/vp-from-vcs"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(verifiableCredentialsServiceOffering), true, false))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(serviceOfferingVpWithProof))));

        stubFor(post(urlEqualTo("/vp-from-vcs"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(verifiableCredentialsResourceOffering), true, false))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(resourceOfferingVpWithProof))));

        stubFor(post(urlEqualTo("/vp-from-vcs"))
                .withRequestBody(matchingJsonPath("$..credentialSubject[0].type", equalTo("gx:compliance")))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(complianceCredentialVpWithProof))));

        stubFor(post(urlEqualTo("/verification"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(serviceOfferingVpWithProof), true, false))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("")));

        stubFor(post(urlEqualTo("/verification"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(resourceOfferingVpWithProof), true, false))
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
                        .post("/v1/send-claims");

        // test
        @SuppressWarnings("unchecked")
        final List<Map<String, Object>> resultVPs = response.getBody().as(List.class);
        assertThat(response.getStatusCode()).isEqualTo(200);
        // 3 VPs are expected: 1 for service offering, 1 for resource offering and 1 for compliance
        assertThat(resultVPs).hasSize(3);

        final VerifiablePresentation resultVPServiceOffering = VerifiablePresentation.fromMap(resultVPs.get(0));
        assertThat(resultVPServiceOffering.getLdProof()).isNotNull();
        @SuppressWarnings("unchecked") final List<Map<String, Object>> vcsServiceOffering = (List<Map<String, Object>>) resultVPServiceOffering.getJsonObject().get("verifiableCredential");
        // 1 service offering VC is expected
        assertThat(vcsServiceOffering).hasSize(1);

        final VerifiablePresentation resultVPResourceOffering = VerifiablePresentation.fromMap(resultVPs.get(1));
        assertThat(resultVPServiceOffering.getLdProof()).isNotNull();
        @SuppressWarnings("unchecked") final List<Map<String, Object>> vcsResourceOffering = (List<Map<String, Object>>) resultVPResourceOffering.getJsonObject().get("verifiableCredential");
        // 5 resource offering VCs are expected
        assertThat(vcsResourceOffering).hasSize(5);

        final VerifiablePresentation resultVPCompliance = VerifiablePresentation.fromMap(resultVPs.get(2));
        assertThat(resultVPServiceOffering.getLdProof()).isNotNull();
        @SuppressWarnings("unchecked") final List<Map<String, Object>> vcsCompliance = (List<Map<String, Object>>) resultVPCompliance.getJsonObject().get("verifiableCredential");
        // 1 compliance VC is expected
        assertThat(vcsCompliance).hasSize(1);
    }

    @Test
    @TestHTTPEndpoint(ClaimComplianceProviderController.class)
    void testGenerateClaimsEndpoint() throws JsonProcessingException {
        // action
        final Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(GenerateClaimsPayload.EXAMPLE_PAYLOAD)
                        .when()
                        .post("/v1/generate-claims");
        // test
        assertThat(response.getStatusCode()).isEqualTo(200);
        final String body = response.getBody().asString();
        assertThat(body).isNotNull();
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonNode = mapper.readTree(body);
        assertThat(jsonNode).isNotNull();
    }

    @Test
    @TestHTTPEndpoint(ClaimComplianceProviderController.class)
    @SuppressWarnings("unchecked")
    void testConfigEndpoint() {
        // action
        final Response response =
                given()
                        .when()
                        .get("/v1/config");
        // test
        assertThat(response.getStatusCode()).isEqualTo(200);
        final String body = response.getBody().asString();
        assertThat(body).isNotNull();
        final List<Map<String, Object>> configurations = response.getBody().as(List.class);
        Map<String, Object> properties;
        assertThat(configurations).hasSize(3);
        Map<String, Object> configuration = configurations.get(0);
        assertThat(configuration).containsEntry("componentName", ComplianceServiceService.class.getSimpleName());
        assertThat((List<Map<String, Object>>)configuration.get("properties")).hasSize(1);
        properties = ((List<Map<String, Object>>)configuration.get("properties")).get(0);
        assertThat(properties).containsEntry("key", "COMPLIANCE_SERVICE_URL")
                .containsEntry("value", "http://localhost:" + this.wireMockServer.port());
        configuration = configurations.get(1);
        assertThat(configuration).containsEntry("componentName", SdCreatorService.class.getSimpleName());
        assertThat((List<Map<String, Object>>)configuration.get("properties")).hasSize(1);
        properties = ((List<Map<String, Object>>)configuration.get("properties")).get(0);
        assertThat(properties).containsEntry("key", "SD_CREATOR_URL");
        assertThat(properties.get("value")).asString().contains(String.valueOf(this.wireMockServer.port()));

        configuration = configurations.get(2);
        assertThat(configuration).containsEntry("componentName", FederatedCatalogueService.class.getSimpleName());
        assertThat((List<Map<String, Object>>)configuration.get("properties")).hasSize(2);
        properties = ((List<Map<String, Object>>)configuration.get("properties")).get(0);
        assertThat(properties).containsEntry("key", "FEDERATED_CATALOGUE_URL");
        assertThat(properties.get("value")).asString().contains(String.valueOf(this.wireMockServer.port()));
        properties = ((List<Map<String, Object>>)configuration.get("properties")).get(1);
        assertThat(properties).containsEntry("key", "KEYCLOAK_URL");
        assertThat(properties.get("value")).asString().contains("/auth/realms/quarkus/token");
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

    private String retrieveType(final Map<String, Object> credentialSubject) {
        assertThat(VpVcUtil.getTypes(credentialSubject)).hasSize(1);
        return VpVcUtil.getTypes(credentialSubject).get(0);
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