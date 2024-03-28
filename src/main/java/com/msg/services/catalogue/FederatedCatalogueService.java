package com.msg.services.catalogue;

import com.msg.utilities.FederatedCatalogueResponse;
import io.quarkus.oidc.client.Tokens;
import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@ApplicationScoped
public class FederatedCatalogueService {
    @Inject
    Tokens tokens;
    @RestClient
    @Inject
    FederatedCatalogueClient federatedCatalogueClient;

    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            return new RuntimeException("The Federated Catalogue responded with HTTP 404");
        } 
        return new RuntimeException(String.format("Unknown issue calling Federated Catalogue, response is %s", response.getStatusInfo().getReasonPhrase()));
    }

    public FederatedCatalogueResponse verify(final Map<String, Object> verifiablePresentation) {
        final String accessToken = tokens.getAccessToken();
        return federatedCatalogueClient.verification(accessToken, verifiablePresentation);
    }
}
