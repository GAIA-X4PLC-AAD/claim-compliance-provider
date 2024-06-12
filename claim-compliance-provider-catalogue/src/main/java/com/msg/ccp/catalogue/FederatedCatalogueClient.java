package com.msg.ccp.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient
public interface FederatedCatalogueClient {
    @POST
    @Path("/verification")
    @Produces("application/json")
    @Consumes("application/json")
    FederatedCatalogueResponse verification(@HeaderParam("Authorization") String accessToken, final VerifiablePresentation verifiablePresentation);


    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            return new RuntimeException("The Federated Catalogue responded with HTTP 404");
        }
        return new RuntimeException(String.format("Unknown issue calling Federated Catalogue, response is %s", response.getStatusInfo().getReasonPhrase()));
    }
}