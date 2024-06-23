package com.msg.ccp.processor;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.interfaces.catalogue.ICatalogueService;
import com.msg.ccp.claims.ClaimsCredentialsService;
import com.msg.ccp.claims.CredentialContainer;
import com.msg.ccp.interfaces.compliance.IComplianceServiceService;
import com.msg.ccp.interfaces.controller.IClaimComplianceProviderService;
import com.msg.ccp.interfaces.sdcreator.ISignerService;
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
    private final ClaimsCredentialsService claimsCredentialsService;

    @Inject
    public VCProcessor(final IComplianceServiceService complianceService, final ISignerService sdCreatorService, final ICatalogueService federatedCatalogueService, final ClaimsCredentialsService claimsCredentialsService) {
        this.complianceService = complianceService;
        this.sdCreatorService = sdCreatorService;
        this.federatedCatalogueService = federatedCatalogueService;
        this.claimsCredentialsService = claimsCredentialsService;
    }

    public VerifiablePresentation process(final Set<Map<String, Object>> claims, final Set<VerifiableCredential> verifiableCredentials) {
        log.info("Processing claims and verifiable credentials");
        final Set<VerifiableCredential> credentials = new HashSet<>(verifiableCredentials);
        credentials.addAll(this.transformClaimsToVCs(claims));

        final CredentialContainer orderedVerifiableCredentials = this.claimsCredentialsService.separateDomainSpecificCredentials(credentials);
        final VerifiableCredential complianceCredential = this.getComplianceCredential(orderedVerifiableCredentials.getVerifiableCredentialsGX());
        final VerifiablePresentation verifiablePresentation = this.mergeVCAndCC(credentials, complianceCredential);

        this.verifyWithFederatedCatalogue(verifiablePresentation);
        log.info("Processing claims and verifiable credentials finished successfully");
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