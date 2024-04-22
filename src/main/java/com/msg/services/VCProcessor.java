package com.msg.services;

import com.msg.services.catalogue.FederatedCatalogueService;
import com.msg.utilities.ClaimCredentialHolder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.danubetech.verifiablecredentials.*;

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

    public VerifiablePresentation process(Set<Map<String, Object>> claimCredentialSet) {
        ClaimCredentialHolder unorderedClaimsAndCredentials = this.claimsCredentialsService.createClaimCredentialHolder(claimCredentialSet);
        log.debug("ClaimsCredentialsMix has been created.");
        Set<VerifiableCredential> credentials = unorderedClaimsAndCredentials.getVerifiableCredentialsUnordered();
        credentials.addAll(this.transformClaimsToVCs(unorderedClaimsAndCredentials.getClaims()));
        log.debug("Credentials have been extracted, claims have been extracted and transformed to credentials.");
        
        // Following code snippet will be utilized as soon as we managed to utilize the compliance Service in conjunction with our SD-Creator deployment
        // ClaimCredentialHolder orderedVerifiableCredentials = this.claimsCredentialsService.separateDomainSpecificCredentials(credentials);
        // Map<String, Object> complianceCredentials = this.getComplianceCredential(orderedVerifiableCredentials.getVerifiableCredentialsGX());
        // log.debug("ComplianceCredentials have been received.");
        // Map<String, Object> verifiablePresentation = this.mergeVCAndCC(credentials, complianceCredentials);
        // log.debug("ComplianceCredentials and VerifiableCredentials have been merged.");

        VerifiablePresentation verifiablePresentation = this.transformVCstoVP(credentials);
        this.verifyWithFederatedCatalogue(verifiablePresentation);
        log.debug("VerifiablePresentation successfully verified by the federated catalogue.");
        return verifiablePresentation;
    }

    public Set<VerifiableCredential> transformClaimsToVCs(Set<Map<String, Object>> claims) {
        return this.sdCreatorService.transformClaimsToVCs(claims);
    }

    public Map<String, Object> getComplianceCredential(Set<Map<String, Object>> verifiableCredentials) {
        Map<String, Object> verifiablePresentationWithoutProof = this.sdCreatorService.wrapCredentialsIntoVerifiablePresentationWithoutProof(verifiableCredentials);
        return this.complianceService.getComplianceCredential(verifiablePresentationWithoutProof);
    }

    public VerifiablePresentation mergeVCAndCC(Set<VerifiableCredential> verifiableCredentials, VerifiableCredential complianceCredential) {
        Set<VerifiableCredential> mergedCredentials = new HashSet<>();
        mergedCredentials.addAll(verifiableCredentials);
        mergedCredentials.add(complianceCredential);
        return this.sdCreatorService.transformVCsToVP(mergedCredentials);
    }

    public VerifiablePresentation transformVCstoVP(Set<VerifiableCredential> verifiableCredentials) {
        return this.sdCreatorService.transformVCsToVP(verifiableCredentials);
    }

    public void verifyWithFederatedCatalogue(final VerifiablePresentation verifiablePresentation) {
        this.federatedCatalogueService.verify(verifiablePresentation);
    }
}