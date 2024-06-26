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
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
        log.info("get access token");
        final String accessToken = "Bearer " + tokens.getAccessToken();

        log.info("call federated catalogue with {}", VpVcUtil.getId(verifiablePresentation));
        try {
            return federatedCatalogueClient.verification(accessToken, verifiablePresentation);
        } catch (final WebApplicationException e) {
            final Response response = e.getResponse();
            if (response.hasEntity()) {
                @SuppressWarnings("unchecked")
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

    public Set<Map<String, Object>> getConfig() {
        Set<Map<String, Object>> configs = new LinkedHashSet<>();
        Map<String, Object> property1 = new LinkedHashMap<>();
        property1.put(KEY_PROPERTY, "FEDERATED_CATALOGUE_URL");
        property1.put(VALUE_PROPERTY, ConfigProvider.getConfig().getValue("quarkus.rest-client.\"com.msg.ccp.sdcreator.SdCreatorClient\".url", String.class));
        configs.add(property1);
        Map<String, Object> property2 = new LinkedHashMap<>();
        property2.put(KEY_PROPERTY, "KEYCLOAK_URL");
        property2.put(VALUE_PROPERTY, ConfigProvider.getConfig().getValue("quarkus.oidc-client.token-path", String.class));
        configs.add(property2);
        return configs;
    }
}
