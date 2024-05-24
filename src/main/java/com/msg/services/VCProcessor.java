package com.msg.services;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.services.catalogue.FederatedCatalogueService;
import com.msg.utilities.ClaimCredentialHolder;
import foundation.identity.jsonld.JsonLDObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

        ClaimCredentialHolder orderedVerifiableCredentials = this.claimsCredentialsService.separateDomainSpecificCredentials(credentials);
        VerifiableCredential complianceCredential = this.getComplianceCredential(orderedVerifiableCredentials.getVerifiableCredentialsGX());
        log.debug("ComplianceCredentials have been received.");
        VerifiablePresentation verifiablePresentation = this.mergeVCAndCC(credentials, complianceCredential);
        log.debug("ComplianceCredentials and VerifiableCredentials have been merged.");

        this.verifyWithFederatedCatalogue(verifiablePresentation);
        log.debug("VerifiablePresentation successfully verified by the federated catalogue.");
        return verifiablePresentation;
    }

    public Set<VerifiableCredential> transformClaimsToVCs(Set<Map<String, Object>> claims) {
        return this.sdCreatorService.transformClaimsToVCs(claims);
    }

    public VerifiableCredential getComplianceCredential(Set<VerifiableCredential> verifiableCredentials) {
        final Set<Map<String, Object>> verifiableCredentialsSet = verifiableCredentials.stream().map(JsonLDObject::getJsonObject).collect(Collectors.toSet());
        Map<String, Object> verifiablePresentationWithoutProof = this.sdCreatorService.wrapCredentialsIntoVerifiablePresentationWithoutProof(verifiableCredentialsSet);
        return VerifiableCredential.fromMap(this.complianceService.getComplianceCredential(verifiablePresentationWithoutProof));
    }

    public VerifiablePresentation mergeVCAndCC(Set<VerifiableCredential> verifiableCredentials, VerifiableCredential complianceCredential) {
        Set<VerifiableCredential> mergedCredentials = new HashSet<>(verifiableCredentials);
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