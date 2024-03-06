package com.msg.controller;

import java.util.Map;
import java.util.Set;

import com.msg.services.*;
import com.msg.utilities.ClaimCredentialHolder;

import io.quarkus.logging.Log;

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

    private VCProcessor verifiableCredentialsProcessor;
    private ClaimsCredentialsService claimsCredentialService;

    @Inject
    public ClaimComplianceProviderController(VCProcessor verifiableCredentialsProcessor, ClaimsCredentialsService claimsCredentialService) {
        this.verifiableCredentialsProcessor = verifiableCredentialsProcessor;
        this.claimsCredentialService = claimsCredentialService;
    }

    @POST
    @Path("/send-claims")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> initiateVCProcessing(Set<Map<String, Object>> claimCredentialSet) {
        ClaimCredentialHolder claimCredentialHolder = this.claimsCredentialService.createClaimCredentialHolder(claimCredentialSet);
        Log.debug("ClaimsCredentialsMix has been created.");
        Set<Map<String, Object>> credentials = claimCredentialHolder.getVerifiableCredentials();
        Log.debug("Credentials have been read.");
        credentials.addAll(verifiableCredentialsProcessor.transformClaimsToVCs(claimCredentialHolder.getClaims()));
        Log.debug("Claims have been transformed to credentials.");

        Set<Map<String, Object>> complianceCredentials = verifiableCredentialsProcessor.getComplianceCredentials(credentials);
        Log.debug("ComplianceCredentials have been received.");
        Map<String, Object> verifiablePresentation = verifiableCredentialsProcessor.mergeVCAndCC(credentials, complianceCredentials);
        Log.debug("ComplianceCredentials and VerifiableCredentials have been merged.");
        return verifiablePresentation;
    }
}