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
    private SdCreatorService sdCreator;
    private FederatedCatalogueService federatedCatalogueService;

    @Inject
    public VCProcessor(ComplianceServiceService complianceService, SdCreatorService sdCreator, final FederatedCatalogueService federatedCatalogueService) {
        this.complianceService = complianceService;
        this.sdCreator = sdCreator;
        this.federatedCatalogueService = federatedCatalogueService;
    }

    public Set<Map<String, Object>> transformClaimsToVCs(Set<Map<String, Object>> claims) {
        return this.sdCreator.transformClaimsToVCs(claims);
    }

    public Set<Map<String, Object>> getComplianceCredentials(Set<Map<String, Object>> verifiableCredentials) {
        return this.complianceService.getComplianceCredentials(verifiableCredentials);
    }

    public Map<String, Object> mergeVCAndCC(Set<Map<String, Object>> verifiableCredentials, Set<Map<String, Object>> complianceCredentials) {
        Set<Map<String, Object>> mergedCredentials = new HashSet<>();
        mergedCredentials.addAll(verifiableCredentials);
        mergedCredentials.addAll(complianceCredentials);
        return this.sdCreator.transformVCsToVP(mergedCredentials);
    }

    public void verifyWithFederatedCatalogue(final Map<String, Object> verifiablePresentation) {
        this.federatedCatalogueService.verify(verifiablePresentation);
    }
}