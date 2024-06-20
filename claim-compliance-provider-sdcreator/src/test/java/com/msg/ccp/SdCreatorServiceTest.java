package com.msg.ccp;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.msg.ccp.sdcreator.SdCreatorService;
import com.msg.ccp.wiremock.InjectWireMock;
import com.msg.ccp.wiremock.WireMockTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@QuarkusTest
@QuarkusTestResource(WireMockTestResource.class)
class SdCreatorServiceTest {

    @Inject
    SdCreatorService sdCreatorService;

    @InjectWireMock
    WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        configureFor("localhost", wireMockServer.port());
    }

    @Test
    @DisplayName("IF claims are passed correctly THEN VCs are created.")
    void testSendClaimsEndpoint() throws JsonProcessingException {
        // prepare
        final Set<Map<String, Object>> claims = createServiceOfferingClaims();

        final ObjectMapper objectMapper = new ObjectMapper();
        for (final Map<String, Object> claim : claims) {
            final String jsonRequest = objectMapper.writeValueAsString(claim);
            final String jsonResponse = objectMapper.writeValueAsString(wrapWithVC(claim));
            stubFor(post(urlEqualTo("/vc-from-claims"))
                    .withRequestBody(equalToJson(jsonRequest))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(jsonResponse)));
        }


        // action
        final Set<VerifiableCredential> verifiableCredentials = sdCreatorService.createVCsFromClaims(claims);

        // test
        assertThat(verifiableCredentials).hasSize(claims.size());
        for (final VerifiableCredential credential : verifiableCredentials) {
            assertThat(verifyType(credential)).isTrue();
        }
    }

    @Test
    @DisplayName("IF exception is thrown inside the REST client THEN it is handled by @ClientExceptionMapper.")
    void testExceptionHandling() {
        // Prepare
        final Map<String, Object> claims = createClaims("service3");
        stubFor(post(urlEqualTo("/vc-from-claims"))
                .willReturn(WireMock.aResponse().withStatus(400).withBody("Bad Request")));

        // action and test
        assertThatThrownBy(() -> {
            sdCreatorService.createVCsFromClaims(Collections.singleton(claims));
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("Unknown issue calling Self Description Creator, response code: 400 message: Bad Request");
    }

    private Set<Map<String, Object>> createServiceOfferingClaims() {
        final Map<String, Object> serviceOfferingClaimsForService1 = createClaims("service1");
        final Map<String, Object> serviceOfferingClaimsForService2 = createClaims("service2");
        return new HashSet<>() {
            {
                add(serviceOfferingClaimsForService1);
                add(serviceOfferingClaimsForService2);
            }
        };
    }

    private static Map<String, Object> createClaims(final String serviceName) {
        return new HashMap<>() {
            {
                put("id", "https://gaia-x.eu/.well-known/" + serviceName + ".json");
                put("type", "gx:ServiceOffering");
                put("gx:providedBy", new HashMap<String, Object>() {
                    {
                        put("id", "https://gaia-x.eu/.well-known/participant.json");
                    }
                });
                put("gx:policy", "");
                put("gx:termsAndConditions", new HashMap<String, Object>() {
                    {
                        put("gx:URL", "http://termsandconds.com");
                        put("gx:hash", "d8402a23de560f5ab34b22d1a142feb9e13b3143");
                    }
                });
                put("gx:dataAccountExport", new HashMap<String, Object>() {
                    {
                        put("gx:requestType", "API");
                        put("gx:accessType", "digital");
                        put("gx:formatType", "application/json");
                    }
                });
            }
        };
    }

    private Map<String, Object> wrapWithVC(final Map<String, Object> stringObjectMap) {
        return new HashMap<>() {
            {
                put("id", "https://gaia-x.eu/.well-known/vcId");
                put("type", "VerifiableCredential");
                put("issuer", "https://gaia-x.eu/.well-known/unitTest.json");
                put("issuanceDate", "2024-05-15T00:00:00Z");
                put("credentialSubject", stringObjectMap);
            }
        };
    }

    private boolean verifyType(final VerifiableCredential credential) {
        return credential.getTypes().contains("VerifiableCredential");
    }
}