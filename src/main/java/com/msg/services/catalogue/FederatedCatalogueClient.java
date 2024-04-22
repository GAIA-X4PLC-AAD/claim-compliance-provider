package com.msg.services.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.utilities.FederatedCatalogueResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient
public interface FederatedCatalogueClient {
    @POST
    @Path("/verification")
    @Produces("application/json")
    @Consumes("application/json")
    FederatedCatalogueResponse verification(@HeaderParam("Authorization") String accessToken, final VerifiablePresentation verifiablePresentation);
}