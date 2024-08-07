package com.msg.ccp.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.interfaces.catalogue.CatalogueResponse;
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
    CatalogueResponse verification(@HeaderParam("Authorization") String accessToken, final VerifiablePresentation verifiablePresentation);
}