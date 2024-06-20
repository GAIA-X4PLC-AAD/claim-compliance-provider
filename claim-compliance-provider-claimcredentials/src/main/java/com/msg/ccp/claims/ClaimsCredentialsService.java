package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
@Slf4j
public class ClaimsCredentialsService implements IClaimsCredentialsService {

    public static final Set<String> GX_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                                                                    "BlockStorage", 
                                                                        "Catalogue", 
                                                                        "ComplianceAssessmentBody", 
                                                                        "ComplianceCertificateClaim", 
                                                                        "ComplianceCertificateCredential", 
                                                                        "ComplianceCertificationScheme", 
                                                                        "ComplianceCriteriaCombination", 
                                                                        "ComplianceCriterion", 
                                                                        "ComplianceLabel", 
                                                                        "ComplianceReferenceManager", 
                                                                        "ComplianceReference", 
                                                                        "Compute", 
                                                                        "Connectivity", 
                                                                        "DataConnector", 
                                                                        "DataResource", 
                                                                        "Database", 
                                                                        "DigitalIdentityWallet", 
                                                                        "FileStorage", 
                                                                        "IdentityAccessManagement", 
                                                                        "IdentityFederation", 
                                                                        "IdentityProvider", 
                                                                        "ImageRegistry", 
                                                                        "Image", 
                                                                        "Infrastructure", 
                                                                        "InstantiatedVirtualResource", 
                                                                        "Interconnection",
                                                                        "LegalParticipant",
                                                                        "LegalPerson",
                                                                        "legalRegistrationNumber",
                                                                        "LinkConnectivity", 
                                                                        "LocatedServiceOffering", 
                                                                        "Location", 
                                                                        "NetworkConnectivity", 
                                                                        "Network", 
                                                                        "NetworkingDevice", 
                                                                        "Node", 
                                                                        "ObjectStorage", 
                                                                        "Orchestration", 
                                                                        "Participant", 
                                                                        "PhysicalConnectivity", 
                                                                        "PhysicalNode", 
                                                                        "PhysicalResource", 
                                                                        "Platform", 
                                                                        "Provider", 
                                                                        "Resource", 
                                                                        "ServiceOffering", 
                                                                        "SoftwareOffering", 
                                                                        "SoftwareResource", 
                                                                        "Storage", 
                                                                        "ThirdPartyComplianceCertificateClaim", 
                                                                        "ThirdPartyComplianceCertificateCredential", 
                                                                        "ThirdPartyComplianceCertificationScheme", 
                                                                        "VerifiableCredentialWallet", 
                                                                        "VirtualNode", 
                                                                        "VirtualResource", 
                                                                        "Wallet", 
                                                                        "Encryption", 
                                                                        "FPGA", 
                                                                        "Flavor", 
                                                                        "Signing", 
                                                                        "VolumeType", 
                                                                        "ServiceAccessPoint", 
                                                                        "CPU", 
                                                                        "GPU",
                                                                        "Disk",
                                                                        "DataAccountExport", 
                                                                        "Endpoint", 
                                                                        "TermsAndConditions",
                                                                        "GaiaXTermsAndConditions",
                                                                        "Standard", 
                                                                        "Measure")));
    protected static final String TYPE_FIELD = "type";
    protected static final String TYPE_FIELD_WITH_AT = "@type";

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
        return CredentialContainer.builder().verifiableCredentialsGX(verifiableCredentialsGX).verifiableCredentialsDomain(verifiableCredentialsDomain).build();
    }

    private List<String> extractTypes(final VerifiableCredential credential) {
        final Map<String, Object> claims = credential.getCredentialSubject().getJsonObject();
        return new ArrayList<>(getTypeValuesAsSet(claims));
    }

    private Set<String> getTypeValuesAsSet(final Map<String, Object> map) {
        final Set<String> result = new HashSet<>();
        final Object typeValue = map.get(TYPE_FIELD);
        final Object atTypeValue = map.get(TYPE_FIELD_WITH_AT);

        if (typeValue instanceof Collection) {
            ((Collection<?>) typeValue).stream().map(Object::toString).forEach(result::add);
        } else if (typeValue != null) {
            result.add(typeValue.toString());
        }

        if (atTypeValue instanceof Collection) {
            ((Collection<?>) atTypeValue).stream().map(Object::toString).forEach(result::add);
        } else if (atTypeValue != null) {
            result.add(atTypeValue.toString());
        }

        return result;
    }

    private List<String> removePrefixes(final List<String> fieldsHavingPrefixes) {
        return fieldsHavingPrefixes.stream()
            .map(s -> s.substring(s.indexOf(":") + 1))
            .toList();
    }
}