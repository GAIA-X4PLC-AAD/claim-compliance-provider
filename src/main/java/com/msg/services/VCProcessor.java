package com.msg.services;

import com.msg.services.catalogue.FederatedCatalogueService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@ApplicationScoped
public class VCProcessor {

    private ComplianceServiceService complianceService;
    private SdCreatorService sdCreatorService;
    private FederatedCatalogueService federatedCatalogueService;

    @Inject
    public VCProcessor(ComplianceServiceService complianceService, SdCreatorService sdCreatorService, final FederatedCatalogueService federatedCatalogueService) {
        this.complianceService = complianceService;
        this.sdCreatorService = sdCreatorService;
        this.federatedCatalogueService = federatedCatalogueService;
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

    public void verifyWithFederatedCatalogue(final Map<String, Object> verifiablePresentation) {
        this.federatedCatalogueService.verify(verifiablePresentation);
    }
}