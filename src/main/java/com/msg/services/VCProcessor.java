package com.msg.services;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class VCProcessor {

    private ComplianceServiceService complianceService;
    private SdCreatorService sdCreatorService;

    @Inject
    public VCProcessor(ComplianceServiceService complianceService, SdCreatorService sdCreator) {
        this.complianceService = complianceService;
        this.sdCreatorService = sdCreator;
    }

    public Set<Map<String, Object>> transformClaimsToVCs(Set<Map<String, Object>> claims) {
        return this.sdCreatorService.transformClaimsToVCs(claims);
    }

    public Map<String, Object> getComplianceCredential(Set<Map<String, Object>> verifiableCredentials) {
        Map<String, Object> verifiablePresentationWithoutProof = this.sdCreatorService.wrapCredentialsIntoVerifiablePresentationWithoutProof(verifiableCredentials);
        return this.complianceService.getComplianceCredential(verifiablePresentationWithoutProof);
    }

    public Map<String, Object> mergeVCAndCC(Set<Map<String, Object>> verifiableCredentials, Map<String, Object> complianceCredential) {
        Set<Map<String, Object>> mergedCredentials = new HashSet<>();
        mergedCredentials.addAll(verifiableCredentials);
        mergedCredentials.add(complianceCredential);
        return this.sdCreatorService.transformVCsToVP(mergedCredentials);
    }
}