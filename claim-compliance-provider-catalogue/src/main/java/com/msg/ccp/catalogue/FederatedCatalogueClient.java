package com.msg.ccp.catalogue;

import com.msg.ccp.interfaces.catalogue.FederatedCatalogueResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Map;

@ApplicationScoped
@RegisterRestClient
public interface FederatedCatalogueClient {
    @POST
    @Path("/verification")
    @Produces("application/json")
    @Consumes("application/json")
    FederatedCatalogueResponse verification(@HeaderParam("Authorization") String accessToken, final Map<String, Object> verifiablePresentation);
}