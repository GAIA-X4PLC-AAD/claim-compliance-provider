package com.msg.services;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.msg.utilities.ClaimCredentialHolder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class ClaimsCredentialsService {

    protected static final String TYPE_FIELD = "type";
    protected static final String VERIFIABLE_CREDENTIAL = "VerifiableCredential";
    protected static final Set<String> GX_TYPES = new HashSet<>(Arrays.asList( 
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
                                                                        "LegalPerson", 
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
                                                                        "Standard", 
                                                                        "Measure"));
    
    public ClaimCredentialHolder createClaimCredentialHolder(Set<Map<String, Object>> claimCredentialSet) {
        Set<Map<String, Object>> claims = new HashSet<>();
        Set<VerifiableCredential> verifiableCredentials = new HashSet<>();

        for(Map<String, Object> claimOrCredential : claimCredentialSet) {
            if(isCredential(claimOrCredential)) {
                verifiableCredentials.add(VerifiableCredential.fromMap(claimOrCredential));
            } else {
                claims.add(claimOrCredential);
            }
        }
        ClaimCredentialHolder claimCredentialHolder = ClaimCredentialHolder.builder().claims(claims).verifiableCredentialsUnordered(verifiableCredentials).build();
        log.info("You provided: {} claims and {} credentials", claimCredentialHolder.getClaims().size(), claimCredentialHolder.getVerifiableCredentialsUnordered().size());
        return claimCredentialHolder;
    }

    public ClaimCredentialHolder separateDomainSpecificCredentials(Set<VerifiableCredential> verifiableCredentials){
        Set<VerifiableCredential> verifiableCredentialsGX = new HashSet<>();
        Set<VerifiableCredential> verifiableCredentialsDomain = new HashSet<>();
        
        for(VerifiableCredential credential : verifiableCredentials) {
            List<String> typeField = credential.getCredentialSubject().getTypes();
            List<String> typeFieldsWithoutPrefix = removePrefixes(typeField);
            log.info(typeFieldsWithoutPrefix.toString());
            if(Collections.disjoint(typeFieldsWithoutPrefix, GX_TYPES)) {
                verifiableCredentialsDomain.add(credential);
            } else {
                verifiableCredentialsGX.add(credential);
            }
        }
        return ClaimCredentialHolder.builder().verifiableCredentialsGX(verifiableCredentialsGX).verifiableCredentialsDomain(verifiableCredentialsDomain).build();
    }

    private boolean isCredential(Map<String, Object> potentialCredential) {
        Set<String> typeCollection = getTypeValuesAsSet(potentialCredential.get(TYPE_FIELD));
        return typeCollection.contains(VERIFIABLE_CREDENTIAL);
    }

    private Set<String> getTypeValuesAsSet(Object typeValue) {
        return (typeValue instanceof Collection) ?
                ((Collection<?>) typeValue).stream().map(Object::toString).collect(Collectors.toSet()) :
                new HashSet<>(Arrays.asList(typeValue.toString()));
    }

    private List<String> removePrefixes(List<String> fieldsHavingPrefixes) {
        return fieldsHavingPrefixes.stream()
            .map(s -> s.substring(s.indexOf(":") + 1))
            .collect(Collectors.toList());
    }
}
