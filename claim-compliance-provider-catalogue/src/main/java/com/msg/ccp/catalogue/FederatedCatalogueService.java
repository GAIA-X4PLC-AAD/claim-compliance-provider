package com.msg.ccp.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.exception.RestClientException;
import com.msg.ccp.interfaces.catalogue.FederatedCatalogueResponse;
import com.msg.ccp.interfaces.catalogue.ICatalogueService;
import com.msg.ccp.util.VpVcUtil;
import io.quarkus.oidc.client.Tokens;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@ApplicationScoped
@Slf4j
public class FederatedCatalogueService implements ICatalogueService {
    private final Tokens tokens;
    private final FederatedCatalogueClient federatedCatalogueClient;

    @Inject
    public FederatedCatalogueService(final Tokens tokens, @RestClient final FederatedCatalogueClient federatedCatalogueClient) {
        this.tokens = tokens;
        this.federatedCatalogueClient = federatedCatalogueClient;
    }

    public FederatedCatalogueResponse verify(final VerifiablePresentation verifiablePresentation) {
        log.info("call federated catalogue");

        final String accessToken = tokens.getAccessToken();
        try {
            return federatedCatalogueClient.verification(accessToken, verifiablePresentation);
        } catch (final WebApplicationException e) {
            // temporary logging to find if there is a problem with the response
            log.warn("An error occurred while calling the federated catalogue", e);
            final Response response = e.getResponse();
            if (response.hasEntity()) {
                final Map<String, Object> errorDetails = response.readEntity(Map.class);
                throw new RestClientException(
                        (String) errorDetails.get("message"),
                        (String) errorDetails.get("code"),
                        e.getMessage(),
                        response.getStatus(),
                        VpVcUtil.getId(verifiablePresentation)
                );
            } else {
                throw new RestClientException(
                        "An error occurred while calling the federated catalogue",
                        "Unknown error",
                        e.getMessage(),
                        response.getStatus(),
                        VpVcUtil.getId(verifiablePresentation)
                );
            }
        }
    }
}
