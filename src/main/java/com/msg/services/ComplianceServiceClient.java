package com.msg.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Map;

@ApplicationScoped
@RegisterRestClient
public interface ComplianceServiceClient {

    @POST
    @Path("/api/credential-offers")
    @Produces("application/json")
    Map<String, Object> postVPGetCC(Map<String, Object> verifiablePresentationWithoutProof);
}