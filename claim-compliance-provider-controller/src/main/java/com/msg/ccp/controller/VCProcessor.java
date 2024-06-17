package com.msg.ccp.controller;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.catalogue.ICatalogueService;
import com.msg.ccp.claims.CredentialContainer;
import com.msg.ccp.claims.IClaimsCredentialsService;
import com.msg.ccp.compliance.IComplianceServiceService;
import com.msg.ccp.sdcreator.ISignerService;
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
public class VCProcessor implements IClaimComplianceProviderService {

    private final IComplianceServiceService complianceService;
    private final ISignerService sdCreatorService;
    private final ICatalogueService federatedCatalogueService;
    private final IClaimsCredentialsService claimsCredentialsService;

    @Inject
    public VCProcessor(final IComplianceServiceService complianceService, final ISignerService sdCreatorService, final ICatalogueService federatedCatalogueService, final IClaimsCredentialsService claimsCredentialsService) {
        this.complianceService = complianceService;
        this.sdCreatorService = sdCreatorService;
        this.federatedCatalogueService = federatedCatalogueService;
        this.claimsCredentialsService = claimsCredentialsService;
    }

    public VerifiablePresentation process(final Set<Map<String, Object>> claims, final Set<VerifiableCredential> verifiableCredentials) {
        log.debug("ClaimsCredentialsMix has been created.");
        final Set<VerifiableCredential> credentials = new HashSet<>(verifiableCredentials);
        credentials.addAll(this.transformClaimsToVCs(claims));
        log.debug("Credentials have been extracted, claims have been extracted and transformed to credentials.");

        final CredentialContainer orderedVerifiableCredentials = this.claimsCredentialsService.separateDomainSpecificCredentials(credentials);
        final VerifiableCredential complianceCredential = this.getComplianceCredential(orderedVerifiableCredentials.getVerifiableCredentialsGX());
        log.debug("ComplianceCredentials have been received.");
        final VerifiablePresentation verifiablePresentation = this.mergeVCAndCC(credentials, complianceCredential);
        log.debug("ComplianceCredentials and VerifiableCredentials have been merged.");

        this.verifyWithFederatedCatalogue(verifiablePresentation);
        log.debug("VerifiablePresentation successfully verified by the federated catalogue.");
        return verifiablePresentation;
    }

    public Set<VerifiableCredential> transformClaimsToVCs(final Set<Map<String, Object>> claims) {
        return this.sdCreatorService.createVCsFromClaims(claims);
    }

    public VerifiableCredential getComplianceCredential(final Set<VerifiableCredential> verifiableCredentials) {
        final Set<Map<String, Object>> verifiableCredentialsSet = verifiableCredentials.stream().map(JsonLDObject::getJsonObject).collect(Collectors.toSet());
        final Map<String, Object> verifiablePresentationWithoutProof = this.sdCreatorService.createVPwithoutProofFromVCs(verifiableCredentialsSet);
        return VerifiableCredential.fromMap(this.complianceService.getComplianceCredential(verifiablePresentationWithoutProof));
    }

    public VerifiablePresentation mergeVCAndCC(final Set<VerifiableCredential> verifiableCredentials, final VerifiableCredential complianceCredential) {
        final Set<VerifiableCredential> mergedCredentials = new HashSet<>(verifiableCredentials);
        mergedCredentials.add(complianceCredential);
        return this.sdCreatorService.createVPfromVCs(mergedCredentials);
    }

    public VerifiablePresentation transformVCstoVP(final Set<VerifiableCredential> verifiableCredentials) {
        return this.sdCreatorService.createVPfromVCs(verifiableCredentials);
    }

    public void verifyWithFederatedCatalogue(final VerifiablePresentation verifiablePresentation) {
        this.federatedCatalogueService.verify(verifiablePresentation);
    }
}