package com.msg.services;

import java.util.Map;
import java.util.Set;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.danubetech.verifiablecredentials.VerifiableCredential;

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
    Map<String, Object> postClaimsGetVCs(Map<String, Object> claims);

    @POST
    @Path("/vp-from-vcs")
    @Produces("application/json")
    Map<String, Object> postVCsGetVP(Set<VerifiableCredential> verifiableCredentials);

    @POST
    @Path("/vp-without-proof-from-vcs")
    @Produces("application/json")
    Map<String, Object> wrapCredentialsIntoVerifiablePresentationWithoutProof(Set<Map<String, Object>> verifiableCredentials);
    
}