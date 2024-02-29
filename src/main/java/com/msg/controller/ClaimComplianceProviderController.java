package com.msg.controller;

import java.util.HashMap;
import java.util.Set;

import com.msg.services.*;
import com.msg.utilities.ClaimsCredentialsMix;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("")
public class ClaimComplianceProviderController {

    private VCProcessor vcProcess;
    private ClaimsCredentialsMix claimsCredentialsMix;

    @Inject
    public ClaimComplianceProviderController(VCProcessor vcProcess, ClaimsCredentialsMix claimsCredentialsMix) {
        this.vcProcess = vcProcess;
        this.claimsCredentialsMix = claimsCredentialsMix;
    }

    @POST
    @Path("/send-claims")
    @Consumes("application/json")
    public HashMap<String, Object> initiateVCProcessing(Set<HashMap<String, Object>> claimCredentialSet) {
        ClaimsCredentialsMix claimsCredentialsMix = this.claimsCredentialsMix.createClaimsCredentialsMix(claimCredentialSet);
        Set<HashMap<String, Object>> credentials = claimsCredentialsMix.getCredentials();
        credentials.addAll(vcProcess.transformClaimsToVCs(claimsCredentialsMix.getClaims()));

        Set<HashMap<String, Object>> complianceCredentials = vcProcess.getComplianceCredentials(credentials);
        HashMap<String, Object> verifiablePresentation = vcProcess.mergeVCAndCC(credentials, complianceCredentials);
        return verifiablePresentation;
    }
}