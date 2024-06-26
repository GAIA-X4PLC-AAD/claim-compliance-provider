package com.msg.ccp.processor;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.claims.ClaimsCredentialsService;
import com.msg.ccp.claims.CredentialContainer;
import com.msg.ccp.exception.CcpException;
import com.msg.ccp.interfaces.catalogue.ICatalogueService;
import com.msg.ccp.interfaces.compliance.IComplianceServiceService;
import com.msg.ccp.interfaces.config.IServiceConfiguration;
import com.msg.ccp.interfaces.controller.IClaimComplianceProviderService;
import com.msg.ccp.interfaces.sdcreator.ISignerService;
import com.msg.ccp.util.VpVcUtil;
import foundation.identity.jsonld.JsonLDObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class VCProcessor implements IClaimComplianceProviderService {

    private static final String COMPONENT_NAME_PROPERTY = "componentName";
    private static final String COMPONENT_PROPERTIES_PROPERTY = "properties";

    private final IComplianceServiceService complianceService;
    private final ISignerService sdCreatorService;
    private final ICatalogueService federatedCatalogueService;
    private final ClaimsCredentialsService claimsCredentialsService;

    private final List<IServiceConfiguration> serviceConfigurations = new ArrayList<>();

    @Inject
    public VCProcessor(final IComplianceServiceService complianceService, final ISignerService sdCreatorService, final ICatalogueService federatedCatalogueService, final ClaimsCredentialsService claimsCredentialsService) {
        this.complianceService = complianceService;
        this.sdCreatorService = sdCreatorService;
        this.federatedCatalogueService = federatedCatalogueService;
        this.claimsCredentialsService = claimsCredentialsService;
        this.serviceConfigurations.add(complianceService);
        this.serviceConfigurations.add(sdCreatorService);
        this.serviceConfigurations.add(federatedCatalogueService);
    }

    public List<VerifiablePresentation> process(final Set<Map<String, Object>> claims, final Set<VerifiableCredential> verifiableCredentials) {
        log.info("Processing claims and verifiable credentials");
        final Set<VerifiableCredential> credentials = new HashSet<>(verifiableCredentials);
        credentials.addAll(this.transformClaimsToVCs(claims));

        final CredentialContainer orderedVerifiableCredentials = this.claimsCredentialsService.separateDomainSpecificCredentials(credentials);
        final VerifiableCredential complianceCredential = this.getComplianceCredential(orderedVerifiableCredentials.verifiableCredentialsGX());
        final List<VerifiablePresentation> verifiablePresentations = this.splitVCsAndCreateVPs(credentials, complianceCredential);

        this.verifyWithFederatedCatalogue(verifiablePresentations);
        log.info("Processing claims and verifiable credentials finished successfully");
        return verifiablePresentations;
    }

    public Set<Map<String, Object>> getConfig() {
        final Set<Map<String, Object>> componentConfigs = new LinkedHashSet<>();
        this.serviceConfigurations.forEach(sc -> {
            Map<String, Object> componentConfig = new HashMap<>();
            componentConfig.put(COMPONENT_NAME_PROPERTY, sc.getClass().getSuperclass().getSimpleName());
            componentConfig.put(COMPONENT_PROPERTIES_PROPERTY, sc.getConfig());
            componentConfigs.add(componentConfig);
        });
        return componentConfigs;
    }

    public Set<VerifiableCredential> transformClaimsToVCs(final Set<Map<String, Object>> claims) {
        return this.sdCreatorService.createVCsFromClaims(claims);
    }

    public VerifiableCredential getComplianceCredential(final Set<VerifiableCredential> verifiableCredentials) {
        final Set<Map<String, Object>> verifiableCredentialsSet = verifiableCredentials.stream().map(JsonLDObject::getJsonObject).collect(Collectors.toSet());
        final Map<String, Object> verifiablePresentationWithoutProof = this.sdCreatorService.createVPwithoutProofFromVCs(verifiableCredentialsSet);
        return VerifiableCredential.fromMap(this.complianceService.getComplianceCredential(verifiablePresentationWithoutProof));
    }

    public List<VerifiablePresentation> splitVCsAndCreateVPs(final Set<VerifiableCredential> verifiableCredentials, final VerifiableCredential complianceCredential) {
        final List<VerifiablePresentation> verifiablePresentations = new ArrayList<>();
        final List<VerifiableCredential> serviceOfferings = findServiceOfferings(verifiableCredentials);
        final List<VerifiableCredential> resourceOfferings = findResourceOfferings(verifiableCredentials);
        VerifiablePresentation serviceOfferingVP = null;
        VerifiablePresentation resourceOfferingVP = null;

        if (!serviceOfferings.isEmpty()) {
            serviceOfferingVP = this.sdCreatorService.createVPfromVCs(new HashSet<>(serviceOfferings));
        }
        if (!resourceOfferings.isEmpty()) {
            resourceOfferingVP = this.sdCreatorService.createVPfromVCs(new HashSet<>(resourceOfferings));
        }
        if (serviceOfferingVP == null && resourceOfferingVP == null) {
            throw new CcpException("Neither ServiceOffering nor ResourceOffering found");
        }

        final VerifiablePresentation complianceVP = this.sdCreatorService.createVPfromVCs(new HashSet<>(Collections.singletonList(complianceCredential)));
        verifiablePresentations.add(serviceOfferingVP);
        verifiablePresentations.add(resourceOfferingVP);
        verifiablePresentations.add(complianceVP);
        return verifiablePresentations;
    }

    public void verifyWithFederatedCatalogue(final List<VerifiablePresentation> verifiablePresentations) {
        // Exclude the last VerifiablePresentation which is the ComplianceCredentialVP
        final List<VerifiablePresentation> presentationsToVerify = verifiablePresentations.subList(0, verifiablePresentations.size() - 1);
        //TODO: refactor this to make it clear that we do not need to verify ComplianceCredentialVP
        for (final VerifiablePresentation verifiablePresentation : presentationsToVerify) {
            this.federatedCatalogueService.verify(verifiablePresentation);
        }
    }

    private List<VerifiableCredential> findServiceOfferings(final Set<VerifiableCredential> verifiableCredentials) {
        final List<VerifiableCredential> serviceOfferings = new ArrayList<>();
        for (final VerifiableCredential vc : verifiableCredentials) {
            final Set<Map<String, Object>> credentialSubjects = VpVcUtil.getCredentialSubjects(vc.toMap());
            for (final Map<String, Object> credentialSubject : credentialSubjects) {
                if ("gx:ServiceOffering".equals(VpVcUtil.getType(credentialSubject))) {
                    serviceOfferings.add(vc);
                }
            }
        }
        return serviceOfferings;
    }

    private List<VerifiableCredential> findResourceOfferings(final Set<VerifiableCredential> verifiableCredentials) {
        final List<VerifiableCredential> resourceOfferings = new ArrayList<>();
        final List<String> excludedTypes = Arrays.asList("gx:ServiceOffering", "gx:LegalParticipant", "gx:legalRegistrationNumber", "gx:GaiaXTermsAndConditions");
        for (final VerifiableCredential vc : verifiableCredentials) {
            final Set<Map<String, Object>> credentialSubjects = VpVcUtil.getCredentialSubjects(vc.toMap());
            for (final Map<String, Object> credentialSubject : credentialSubjects) {
                if (!excludedTypes.contains(VpVcUtil.getType(credentialSubject))) {
                    resourceOfferings.add(vc);
                }
            }
        }
        return resourceOfferings;
    }
}