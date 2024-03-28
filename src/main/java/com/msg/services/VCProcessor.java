package com.msg.services;

import com.msg.services.catalogue.FederatedCatalogueService;
import com.msg.utilities.ClaimCredentialHolder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@ApplicationScoped
@Slf4j
public class VCProcessor {

    private ComplianceServiceService complianceService;
    private SdCreatorService sdCreatorService;
    private FederatedCatalogueService federatedCatalogueService;
    private ClaimsCredentialsService claimsCredentialsService;

    @Inject
    public VCProcessor(ComplianceServiceService complianceService, SdCreatorService sdCreatorService, final FederatedCatalogueService federatedCatalogueService, ClaimsCredentialsService claimsCredentialsService) {
        this.complianceService = complianceService;
        this.sdCreatorService = sdCreatorService;
        this.federatedCatalogueService = federatedCatalogueService;
        this.claimsCredentialsService = claimsCredentialsService;
    }

    public Map<String, Object> process(Set<Map<String, Object>> claimCredentialSet) {
        ClaimCredentialHolder claimCredentialHolder = this.claimsCredentialsService.createClaimCredentialHolder(claimCredentialSet);
        log.debug("ClaimsCredentialsMix has been created.");
        Set<Map<String, Object>> credentials = claimCredentialHolder.getVerifiableCredentials();
        log.debug("Credentials have been read.");
        credentials.addAll(this.transformClaimsToVCs(claimCredentialHolder.getClaims()));
        log.debug("Claims have been transformed to credentials.");

        Map<String, Object> complianceCredentials = this.getComplianceCredential(credentials);
        log.debug("ComplianceCredentials have been received.");
        Map<String, Object> verifiablePresentation = this.mergeVCAndCC(credentials, complianceCredentials);
        log.debug("ComplianceCredentials and VerifiableCredentials have been merged.");
        this.verifyWithFederatedCatalogue(verifiablePresentation);
        log.debug("VerifiablePresentation successfully verified by the federated catalogue.");
        return verifiablePresentation;
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