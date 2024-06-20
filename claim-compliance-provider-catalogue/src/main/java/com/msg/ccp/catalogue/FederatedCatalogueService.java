package com.msg.ccp.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.interfaces.catalogue.FederatedCatalogueResponse;
import com.msg.ccp.interfaces.catalogue.ICatalogueService;
import io.quarkus.oidc.client.Tokens;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class FederatedCatalogueService implements ICatalogueService {
    private final Tokens tokens;
    private final FederatedCatalogueClient federatedCatalogueClient;

    @Inject
    public FederatedCatalogueService(final Tokens tokens, @RestClient final FederatedCatalogueClient federatedCatalogueClient) {
        this.tokens = tokens;
        this.federatedCatalogueClient = federatedCatalogueClient;
    }

    public FederatedCatalogueResponse verify(final VerifiablePresentation verifiablePresentation) {
        final String accessToken = tokens.getAccessToken();
        return federatedCatalogueClient.verification(accessToken, verifiablePresentation);
    }
}
