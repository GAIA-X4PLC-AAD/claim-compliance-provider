package com.msg.controller;

import com.msg.services.VCProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;
import java.util.Set;

@ApplicationScoped
@Path("")
public class ClaimComplianceProviderController {

    private VCProcessor verifiableCredentialsProcessor;

    @Inject
    public ClaimComplianceProviderController(VCProcessor verifiableCredentialsProcessor) {
        this.verifiableCredentialsProcessor = verifiableCredentialsProcessor;
    }

    @POST
    @Path("/send-claims")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> initiateVCProcessing(Set<Map<String, Object>> claimCredentialSet) {
        return verifiableCredentialsProcessor.process(claimCredentialSet);
    }
}