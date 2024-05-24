package com.msg.services.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.utilities.FederatedCatalogueResponse;
import io.quarkus.oidc.client.Tokens;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class FederatedCatalogueService {
    @Inject
    Tokens tokens;
    @RestClient
    @Inject
    FederatedCatalogueClient federatedCatalogueClient;

    public FederatedCatalogueResponse verify(final VerifiablePresentation verifiablePresentation) {
        final String accessToken = tokens.getAccessToken();
        return federatedCatalogueClient.verification(accessToken, verifiablePresentation);
    }
}
