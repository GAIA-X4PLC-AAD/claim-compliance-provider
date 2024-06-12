package com.msg.ccp.compliance;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
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
import java.util.Collections;
import java.util.Map;

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
        String jsonRequest = Files.readString(Path.of("src/test/resources/verifiablePresentationRequest.json"));
        String jsonResponse = Files.readString(Path.of("src/test/resources/complianceCredentialsResponse.json"));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestMap = mapper.readValue(jsonRequest, new TypeReference<>() {
        });

        stubFor(post(urlEqualTo("/api/credential-offers"))
                .withRequestBody(equalToJson(jsonRequest))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));

        // action
        Map<String, Object> complianceCredential = complianceServiceService.getComplianceCredential(requestMap);

        // test
        assertThat(complianceCredential).isNotNull();
        assertThat(complianceCredential.get("credentialSubject")).isNotNull();
    }

    @Test
    @DisplayName("IF exception is thrown inside the REST client THEN it is handled by @ClientExceptionMapper.")
    void testExceptionHandling() {
        // Prepare
        stubFor(post(urlEqualTo("/api/credential-offers"))
                .willReturn(WireMock.aResponse().withStatus(400).withBody("Bad Request")));

        // action and test
        assertThatThrownBy(() -> {
            complianceServiceService.getComplianceCredential(Collections.emptyMap());
        }).isInstanceOf(RuntimeException.class)
                .hasMessageStartingWith("Received: 'Bad Request, status code 400' when invoking: Rest Client method: 'com.msg.ccp.compliance.ComplianceServiceClient");
    }

    private boolean verifyType(final VerifiableCredential credential) {
        return credential.getTypes().contains("VerifiableCredential");
    }
}