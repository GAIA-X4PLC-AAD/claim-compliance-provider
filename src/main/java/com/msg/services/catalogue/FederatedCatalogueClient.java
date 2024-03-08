package com.msg.services.catalogue;

import com.msg.utilities.FederatedCatalogueResponse;
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