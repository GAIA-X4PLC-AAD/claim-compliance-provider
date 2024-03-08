package com.msg.services.catalogue;

import com.msg.utilities.FederatedCatalogueResponse;
import io.quarkus.oidc.client.Tokens;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@ApplicationScoped
public class FederatedCatalogueService {
    @Inject
    Tokens tokens;
    @RestClient
    @Inject
    FederatedCatalogueClient federatedCatalogueClient;

    public FederatedCatalogueResponse verify(final Map<String, Object> verifiablePresentation) {
        final String accessToken = tokens.getAccessToken();
        return federatedCatalogueClient.verification(accessToken, verifiablePresentation);
    }
}
