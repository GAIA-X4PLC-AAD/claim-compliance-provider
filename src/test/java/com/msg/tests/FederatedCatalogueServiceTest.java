package com.msg.tests;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msg.services.catalogue.FederatedCatalogueClient;
import com.msg.services.catalogue.FederatedCatalogueService;
import com.msg.utilities.FederatedCatalogueResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.apache.groovy.util.Maps;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
class FederatedCatalogueServiceTest {
    private static final String PRESENTATION_200 = "presentationReturning200";
    private static final String PRESENTATION_400 = "presentationReturning400";
    private static final VerifiablePresentation VALID_PAYLOAD = VerifiablePresentation.fromMap(Maps.of(PRESENTATION_200, PRESENTATION_200));
    private static final VerifiablePresentation INVALID_PAYLOAD = VerifiablePresentation.fromMap(Maps.of(PRESENTATION_400, PRESENTATION_400));

    @InjectMock
    @RestClient
    FederatedCatalogueClient federatedCatalogueClientMock;
    @Inject
    FederatedCatalogueService federatedCatalogueService;

    @Test
    @DisplayName("IF VP is valid THEN request is successful.")
    void successfulVerification() throws JsonProcessingException {
        // prepare
        Mockito.when(federatedCatalogueClientMock.verification(anyString(), eq(VALID_PAYLOAD))).thenReturn(createFederatedCatalogueResponse());

        // action
        final FederatedCatalogueResponse response = federatedCatalogueService.verify(VALID_PAYLOAD);

        // test
        assertThat(response).isNotNull();
        assertThat(response.getLifecycleStatus()).isEqualTo("active");
    }

    @Test
    @DisplayName("IF VP is invalid THEN an exception is thrown.")
    void failingVerification() {
        // prepare
        Mockito.when(federatedCatalogueClientMock.verification(anyString(), eq(INVALID_PAYLOAD))).thenThrow(new NotFoundException());

        // action and test
        assertThatThrownBy(() -> {
            federatedCatalogueService.verify(INVALID_PAYLOAD);
        }).isInstanceOf(NotFoundException.class);
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
