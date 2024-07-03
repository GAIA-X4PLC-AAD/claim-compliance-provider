package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.msg.ccp.exception.ValidationException;
import com.msg.ccp.util.VpVcUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.*;

@ApplicationScoped
@Slf4j
public class ClaimsCredentialsService {

    private static final String SERVICE_OFFERING = "ServiceOffering";
    private static final String LEGAL_PARTICIPANT = "LegalParticipant";
    private static final String LEGAL_REGISTRATION_NUMBER = "legalRegistrationNumber";
    private static final String GAIA_X_TERMS_AND_CONDITIONS = "GaiaXTermsAndConditions";


    public static final Set<String> GX_TYPES = Set.of("BlockStorage", "Catalogue", "ComplianceAssessmentBody",
            "ComplianceCertificateClaim", "ComplianceCertificateCredential", "ComplianceCertificationScheme",
            "ComplianceCriteriaCombination", "ComplianceCriterion", "ComplianceLabel", "ComplianceReferenceManager",
            "ComplianceReference", "Compute", "Connectivity", "DataConnector", "DataResource", "Database",
            "DigitalIdentityWallet", "FileStorage", "IdentityAccessManagement", "IdentityFederation",
            "IdentityProvider", "ImageRegistry", "Image", "Infrastructure", "InstantiatedVirtualResource",
            "Interconnection", LEGAL_PARTICIPANT, "LegalPerson", LEGAL_REGISTRATION_NUMBER, "LinkConnectivity",
            "LocatedServiceOffering", "Location", "NetworkConnectivity", "Network", "NetworkingDevice", "Node",
            "ObjectStorage", "Orchestration", "Participant", "PhysicalConnectivity", "PhysicalNode",
            "PhysicalResource", "Platform", "Provider", "Resource", SERVICE_OFFERING, "SoftwareOffering",
            "SoftwareResource", "Storage", "ThirdPartyComplianceCertificateClaim",
            "ThirdPartyComplianceCertificateCredential", "ThirdPartyComplianceCertificationScheme",
            "VerifiableCredentialWallet", "VirtualNode", "VirtualResource", "Wallet", "Encryption", "FPGA",
            "Flavor", "Signing", "VolumeType", "ServiceAccessPoint", "CPU", "GPU", "Disk", "DataAccountExport",
            "Endpoint", "TermsAndConditions", GAIA_X_TERMS_AND_CONDITIONS, "Standard", "Measure");

    public CredentialContainer separateDomainSpecificCredentials(final Set<VerifiableCredential> verifiableCredentials){
        final Set<VerifiableCredential> verifiableCredentialsGX = new HashSet<>();
        final Set<VerifiableCredential> verifiableCredentialsDomain = new HashSet<>();
        
        for(final VerifiableCredential credential : verifiableCredentials) {
            final List<String> typeField = extractTypes(credential);
            final List<String> typeFieldsWithoutPrefix = removePrefixes(typeField);
            log.info(typeFieldsWithoutPrefix.toString());
            if(Collections.disjoint(typeFieldsWithoutPrefix, GX_TYPES)) {
                verifiableCredentialsDomain.add(credential);
            } else {
                verifiableCredentialsGX.add(credential);
            }
        }

        final CredentialContainer credentialContainer = new CredentialContainer(verifiableCredentialsDomain, verifiableCredentialsGX);
        final List<String> foundIds = getDomainSpecificCredentialSubjectIdsNotExistentInGxCredentialSubjectIds(credentialContainer);
        if (!foundIds.isEmpty()) {
            log.warn("The following domain specific credential subject ids are not existent in the GX credential subject ids: " + foundIds);
        }
        return credentialContainer;
    }

    public List<VerifiableCredential> findServiceOfferings(final Set<VerifiableCredential> verifiableCredentials) {
        final List<VerifiableCredential> serviceOfferings = new ArrayList<>();
        for (final VerifiableCredential vc : verifiableCredentials) {
            final Set<Map<String, Object>> credentialSubjects = VpVcUtil.getCredentialSubjects(vc.toMap());
            for (final Map<String, Object> credentialSubject : credentialSubjects) {
                final List<String> types = VpVcUtil.getTypes(credentialSubject);
                final List<String> typesWithoutPrefixes = removePrefixes(types);
                if (typesWithoutPrefixes.contains(SERVICE_OFFERING)) {
                    serviceOfferings.add(vc);
                }
            }
        }
        return serviceOfferings;
    }

    public List<VerifiableCredential> findResourceOfferings(final Set<VerifiableCredential> verifiableCredentials) {
        final List<VerifiableCredential> resourceOfferings = new ArrayList<>();
        final List<String> excludedTypes = Arrays.asList(SERVICE_OFFERING, LEGAL_PARTICIPANT, LEGAL_REGISTRATION_NUMBER,
                GAIA_X_TERMS_AND_CONDITIONS);
        for (final VerifiableCredential vc : verifiableCredentials) {
            final Set<Map<String, Object>> credentialSubjects = VpVcUtil.getCredentialSubjects(vc.toMap());
            for (final Map<String, Object> credentialSubject : credentialSubjects) {
                final List<String> types = VpVcUtil.getTypes(credentialSubject);
                final List<String> typesWithoutPrefixes = removePrefixes(types);
                if (typesWithoutPrefixes.stream().noneMatch(excludedTypes::contains)) {
                    resourceOfferings.add(vc);
                }
            }
        }
        return resourceOfferings;
    }

    public String getIssuerOfParticipant(final Set<VerifiableCredential> verifiableCredentials) {
        if (verifiableCredentials.isEmpty()) {
            throw new ValidationException("No verifiable credentials provided!");
        }
        return verifiableCredentials.stream()
                .filter(vc -> VpVcUtil.getCredentialSubjects(vc.toMap()).stream()
                        .anyMatch(cs -> removePrefixes(VpVcUtil.getTypes(cs)).contains(LEGAL_PARTICIPANT)))
                .map(vc -> {
                    final URI issuer = vc.getIssuer();
                    if (issuer == null) {
                        throw new ValidationException("No Issuer is set in the LegalParticipant!");
                    }
                    return issuer.toString();
                })
                .findFirst()
                .orElseThrow(() -> new ValidationException("No LegalParticipant found in list of verifiableCredentials!"));
    }

    private List<String> extractTypes(final VerifiableCredential credential) {
        final List<String> types = new ArrayList<>();
        final Map<String, Object> credentialMap = credential.toMap();
        final Set<Map<String, Object>> credentialSubjects = VpVcUtil.getCredentialSubjects(credentialMap);
        for (final Map<String, Object> credentialSubject : credentialSubjects) {
            types.addAll(VpVcUtil.getTypes(credentialSubject));
        }
        return types;
    }

    private List<String> removePrefixes(final List<String> fieldsHavingPrefixes) {
        return fieldsHavingPrefixes.stream()
            .map(s -> s.substring(s.indexOf(":") + 1))
            .toList();
    }

    protected List<String> getDomainSpecificCredentialSubjectIdsNotExistentInGxCredentialSubjectIds(final CredentialContainer credentialContainer) {
        final List<String> domainSpecificIds = collectCredentialSubjectIds(credentialContainer.verifiableCredentialsDomain());
        final List<String> gxIds = collectCredentialSubjectIds(credentialContainer.verifiableCredentialsGX());
        domainSpecificIds.removeAll(gxIds);
        return domainSpecificIds;
    }

    private List<String> collectCredentialSubjectIds(final Set<VerifiableCredential> verifiableCredentials) {
        final List<String> ids = new ArrayList<>();
        if (verifiableCredentials != null) {
            verifiableCredentials.forEach(verifiableCredential -> {
                final Set<Map<String, Object>> credentialSubjects = VpVcUtil.getCredentialSubjects(verifiableCredential.toMap());
                credentialSubjects.forEach(credentialSubject -> ids.add(VpVcUtil.getId(credentialSubject)));
            });
        }
        return ids;
    }
}
