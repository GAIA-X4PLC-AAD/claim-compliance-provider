package com.msg.ccp.controller;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("")
public class ClaimComplianceProviderController {

    private final IClaimComplianceProviderService verifiableCredentialsProcessor;

    @Inject
    public ClaimComplianceProviderController(VCProcessor verifiableCredentialsProcessor) {
        this.verifiableCredentialsProcessor = verifiableCredentialsProcessor;
    }

    @POST
    @Path("/send-claims")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public VerifiablePresentation initiateVCProcessing(final Payload payload) {
        return verifiableCredentialsProcessor.process(payload.getClaims(), payload.getVerifiableCredentials());
    }
}