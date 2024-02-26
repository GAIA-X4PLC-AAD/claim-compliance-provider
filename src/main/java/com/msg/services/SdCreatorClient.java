package com.msg.services;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterRestClient
public interface SdCreatorClient {
    
    @POST
    @Path("/vc-from-claims")
    @Produces("application/json")
    HashMap<String, Object> postClaimsGetVCs(HashMap<String, Object> claims);

    @POST
    @Path("/vp-from-vcs")
    @Produces("application/json")
    HashMap<String, Object> postVCsGetVP(Set<HashMap<String, Object>> verifiableCredentials);
}