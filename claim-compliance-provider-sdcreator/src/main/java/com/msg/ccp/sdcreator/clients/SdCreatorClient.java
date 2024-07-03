package com.msg.ccp.sdcreator.clients;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.Map;
import java.util.Set;

/**
 * REST client for the sd-creator service.
 * Note there will be several clients deriving from this interface since there is a sd-creator service for each project partner.
 * Please also see javadoc in ClientProvider for more information.
 */
public interface SdCreatorClient {

    @POST
    @Path("/vc-from-claims")
    @Produces("application/json")
    Map<String, Object> postClaimsGetVCs(Map<String, Object> claims);

    @POST
    @Path("/vp-from-vcs")
    @Produces("application/json")
    VerifiablePresentation postVCsGetVP(Set<VerifiableCredential> verifiableCredentials);

    @POST
    @Path("/vp-without-proof-from-vcs")
    @Produces("application/json")
    VerifiablePresentation wrapCredentialsIntoVerifiablePresentationWithoutProof(Set<VerifiableCredential> verifiableCredentials);
}
