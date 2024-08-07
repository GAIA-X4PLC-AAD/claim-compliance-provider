package com.msg.ccp.compliance;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.msg.ccp.exception.RestClientException;
import com.msg.ccp.wiremock.InjectWireMock;
import com.msg.ccp.wiremock.WireMockTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
@QuarkusTestResource(WireMockTestResource.class)
class ComplianceServiceServiceTest {
    @Inject
    ComplianceServiceService complianceServiceService;

    @InjectWireMock
    WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        configureFor("localhost", wireMockServer.port());
    }

    @Test
    @DisplayName("IF valid request was sent THEN the response contains a field credentialSubject.")
    void testEndpoint() throws IOException {
        // prepare
        final String jsonRequest = Files.readString(Path.of("src/test/resources/verifiablePresentationRequest.json"));
        final String jsonResponse = Files.readString(Path.of("src/test/resources/complianceCredentialsResponse.json"));
        final ObjectMapper mapper = new ObjectMapper();
        final VerifiablePresentation verifiablePresentation = VerifiablePresentation.fromMap(mapper.readValue(jsonRequest, new TypeReference<>() {
        }));

        stubFor(post(urlEqualTo("/api/credential-offers"))
                .withRequestBody(equalToJson(jsonRequest))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));

        // action
        final VerifiableCredential complianceCredential = complianceServiceService.getComplianceCredential(verifiablePresentation);

        // test
        assertThat(complianceCredential).isNotNull();
        assertThat(complianceCredential.toMap().get("credentialSubject")).isNotNull();
    }

    @Test
    @DisplayName("IF exception is thrown inside the REST client THEN it is handled by @RestClientExceptionMapper.")
    void testExceptionHandling() {
        // Prepare
        stubFor(post(urlEqualTo("/api/credential-offers"))
                .willReturn(WireMock.aResponse().withStatus(409).
                        withHeader("content-type", "application/json").withBody("""
                                {
                                  "message": "Some error message",
                                  "error": "Conflict",
                                  "statusCode": 409
                                }""")));

        // action and test
        assertThatThrownBy(() -> complianceServiceService.getComplianceCredential(new VerifiablePresentation())).isInstanceOf(RestClientException.class)
                .hasMessageStartingWith("Some error message");
    }

    @Test
    @DisplayName("IF body is null in case of exception thrown inside the REST client THEN it is handled by @RestClientExceptionMapper.")
    void testExceptionHandlingWithEmptyBody() {
        // Prepare
        stubFor(post(urlEqualTo("/api/credential-offers"))
                .willReturn(WireMock.aResponse().withStatus(409).
                        withHeader("content-type", "application/json")));

        // action and test
        assertThatThrownBy(() -> complianceServiceService.getComplianceCredential(new VerifiablePresentation())).isInstanceOf(RestClientException.class)
                .hasMessage("An error occurred while calling the compliance service");
    }
}