package com.msg.ccp.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.msg.ccp.interfaces.catalogue.FederatedCatalogueResponse;
import com.msg.ccp.wiremock.InjectWireMock;
import com.msg.ccp.wiremock.WireMockTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import jakarta.inject.Inject;
import org.apache.groovy.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
@QuarkusTestResource(WireMockTestResource.class)
class FederatedCatalogueServiceTest {
    private static final String PRESENTATION_200 = "presentationReturning200";
    private static final String PRESENTATION_400 = "presentationReturning400";
    private static final VerifiablePresentation VALID_PAYLOAD = VerifiablePresentation.fromMap(Maps.of(PRESENTATION_200, PRESENTATION_200));
    private static final VerifiablePresentation INVALID_PAYLOAD = VerifiablePresentation.fromMap(Maps.of(PRESENTATION_400, PRESENTATION_400));

    @InjectWireMock
    WireMockServer wireMockServer;

    @Inject
    FederatedCatalogueService federatedCatalogueService;

    @BeforeEach
    public void setup() {
        configureFor("localhost", wireMockServer.port());
    }

    @Test
    @DisplayName("IF VP is valid THEN request is successful.")
    void successfulVerification() throws JsonProcessingException {
        // prepare
        ObjectMapper objectMapper = new ObjectMapper();
        final String jsonRequest = objectMapper.writeValueAsString(VALID_PAYLOAD);
        final String jsonResponse = objectMapper.writeValueAsString(createFederatedCatalogueResponse());

        stubFor(post(urlEqualTo("/verification"))
                .withRequestBody(equalToJson(jsonRequest))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));

        // action
        final FederatedCatalogueResponse response = federatedCatalogueService.verify(VALID_PAYLOAD);

        // test
        assertThat(response).isNotNull();
        assertThat(response.getLifecycleStatus()).isEqualTo("active");
    }

    @Test
    @DisplayName("IF VP is invalid THEN an exception is thrown.")
    void failingVerification() throws JsonProcessingException {
        // prepare
        ObjectMapper objectMapper = new ObjectMapper();
        final String jsonRequest = objectMapper.writeValueAsString(INVALID_PAYLOAD);

        stubFor(post(urlEqualTo("/verification"))
                .withRequestBody(equalToJson(jsonRequest))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("Bad Request")));

        // action and test
        assertThatThrownBy(() -> {
            federatedCatalogueService.verify(INVALID_PAYLOAD);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("The Federated Catalogue responded with HTTP 404");
    }

    private FederatedCatalogueResponse createFederatedCatalogueResponse() throws JsonProcessingException {
        final String responseString = """
                {
                    "verificationTimestamp": "2024-03-07T18:23:13.658412618Z",
                    "lifecycleStatus": "active",
                    "issuer": "did:web:compliance.lab.gaia-x.eu",
                    "issuedDateTime": "2023-12-06T12:43:21Z",
                    "validatorDids": [
                        "did:web:compliance.lab.gaia-x.eu"
                    ]
                }
                """;
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseString, FederatedCatalogueResponse.class);
    }
}
