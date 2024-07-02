package com.msg.ccp.processor;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.claims.ClaimsCredentialsService;
import com.msg.ccp.claims.CredentialContainer;
import com.msg.ccp.claims.PresentationContainer;
import com.msg.ccp.exception.CcpException;
import com.msg.ccp.interfaces.catalogue.ICatalogueService;
import com.msg.ccp.interfaces.compliance.IComplianceServiceService;
import com.msg.ccp.interfaces.config.IServiceConfiguration;
import com.msg.ccp.interfaces.controller.IClaimComplianceProviderService;
import com.msg.ccp.interfaces.signer.ISignerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@ApplicationScoped
@Slf4j
public class VCProcessor implements IClaimComplianceProviderService {

    private static final String COMPONENT_NAME_PROPERTY = "componentName";
    private static final String COMPONENT_PROPERTIES_PROPERTY = "properties";

    private final IComplianceServiceService complianceService;
    private final ISignerService signerService;
    private final ICatalogueService catalogueService;
    private final ClaimsCredentialsService claimsCredentialsService;

    private final List<IServiceConfiguration> serviceConfigurations = new ArrayList<>();

    @Inject
    public VCProcessor(final IComplianceServiceService complianceService, final ISignerService signerService, final ICatalogueService catalogueService, final ClaimsCredentialsService claimsCredentialsService) {
        this.complianceService = complianceService;
        this.signerService = signerService;
        this.catalogueService = catalogueService;
        this.claimsCredentialsService = claimsCredentialsService;
        this.serviceConfigurations.add(complianceService);
        this.serviceConfigurations.add(signerService);
        this.serviceConfigurations.add(catalogueService);
    }

    public List<VerifiablePresentation> process(final Set<Map<String, Object>> claims, final Set<VerifiableCredential> verifiableCredentials) {
        log.info("Processing claims and verifiable credentials");
        final Set<VerifiableCredential> credentials = new HashSet<>(verifiableCredentials);
        credentials.addAll(this.transformClaimsToVCs(claims));

        final CredentialContainer orderedVerifiableCredentials = this.claimsCredentialsService.separateDomainSpecificCredentials(credentials);
        final VerifiableCredential complianceCredential = this.getComplianceCredential(orderedVerifiableCredentials.verifiableCredentialsGX());
        final PresentationContainer presentationContainer = this.splitVCsAndCreateVPs(credentials, complianceCredential);

        this.verifyWithFederatedCatalogue(presentationContainer);
        log.info("Processing claims and verifiable credentials finished successfully");
        return presentationContainer.toList();
    }

    public Set<Map<String, Object>> getConfig() {
        final Set<Map<String, Object>> componentConfigs = new LinkedHashSet<>();
        this.serviceConfigurations.forEach(sc -> {
            final Map<String, Object> componentConfig = new HashMap<>();
            componentConfig.put(COMPONENT_NAME_PROPERTY, sc.getClass().getSuperclass().getSimpleName());
            componentConfig.put(COMPONENT_PROPERTIES_PROPERTY, sc.getConfig());
            componentConfigs.add(componentConfig);
        });
        return componentConfigs;
    }

    private Set<VerifiableCredential> transformClaimsToVCs(final Set<Map<String, Object>> claims) {
        return this.signerService.createVCsFromClaims(claims);
    }

    private VerifiableCredential getComplianceCredential(final Set<VerifiableCredential> verifiableCredentials) {
        final VerifiablePresentation verifiablePresentationWithoutProof = this.signerService.createVPwithoutProofFromVCs(verifiableCredentials);
        return this.complianceService.getComplianceCredential(verifiablePresentationWithoutProof);
    }

    private PresentationContainer splitVCsAndCreateVPs(final Set<VerifiableCredential> verifiableCredentials, final VerifiableCredential complianceCredential) {
        final List<VerifiableCredential> serviceOfferings = this.claimsCredentialsService.findServiceOfferings(verifiableCredentials);
        final List<VerifiableCredential> resourceOfferings = this.claimsCredentialsService.findResourceOfferings(verifiableCredentials);
        VerifiablePresentation serviceOfferingVP = null;
        VerifiablePresentation resourceOfferingVP = null;

        if (!serviceOfferings.isEmpty()) {
            serviceOfferingVP = this.signerService.createVPfromVCs(new HashSet<>(serviceOfferings));
        }
        if (!resourceOfferings.isEmpty()) {
            resourceOfferingVP = this.signerService.createVPfromVCs(new HashSet<>(resourceOfferings));
        }
        if (serviceOfferingVP == null && resourceOfferingVP == null) {
            throw new CcpException("Neither ServiceOffering nor ResourceOffering found");
        }

        final VerifiablePresentation complianceVP = this.signerService.createVPfromVCs(new HashSet<>(Collections.singletonList(complianceCredential)));
        return new PresentationContainer(serviceOfferingVP, resourceOfferingVP, complianceVP);
    }

    public void verifyWithFederatedCatalogue(final PresentationContainer presentationContainer) {
        // Exclude the last VerifiablePresentation which is the ComplianceCredentialVP
        if (presentationContainer.serviceOfferings() != null) {
            this.catalogueService.invoke(presentationContainer.serviceOfferings());
        }
        if (presentationContainer.resources() != null) {
            this.catalogueService.invoke(presentationContainer.resources());
        }
    }
}