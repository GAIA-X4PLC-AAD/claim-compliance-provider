package com.msg.controller;

import com.msg.services.ClaimsCredentialsService;
import com.msg.services.VCProcessor;
import com.msg.utilities.ClaimCredentialHolder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

@ApplicationScoped
@Slf4j
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
        log.debug("ClaimsCredentialsMix has been created.");
        Set<Map<String, Object>> credentials = claimCredentialHolder.getVerifiableCredentials();
        log.debug("Credentials have been read.");
        credentials.addAll(verifiableCredentialsProcessor.transformClaimsToVCs(claimCredentialHolder.getClaims()));
        log.debug("Claims have been transformed to credentials.");

        Map<String, Object> complianceCredentials = verifiableCredentialsProcessor.getComplianceCredential(credentials);
        log.debug("ComplianceCredentials have been received.");
        Map<String, Object> verifiablePresentation = verifiableCredentialsProcessor.mergeVCAndCC(credentials, complianceCredentials);
        log.debug("ComplianceCredentials and VerifiableCredentials have been merged.");
        verifiableCredentialsProcessor.verifyWithFederatedCatalogue(verifiablePresentation);
        log.debug("VerifiablePresentation successfully verified by the federated catalogue.");
        return verifiablePresentation;
    }
}