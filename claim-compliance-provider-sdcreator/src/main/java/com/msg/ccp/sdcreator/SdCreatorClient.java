package com.msg.ccp.sdcreator;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Map;
import java.util.Set;

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