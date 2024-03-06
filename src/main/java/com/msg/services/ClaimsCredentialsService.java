package com.msg.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.msg.utilities.ClaimCredentialHolder;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class ClaimsCredentialsService {

    public final String TYPEFIELD = "type";
    public final String VERIFIABLECREDENTIAL = "VerifiableCredential";
    
    public ClaimCredentialHolder createClaimCredentialHolder(Set<Map<String, Object>> claimCredentialMix) {
        Set<Map<String, Object>> claims = new HashSet<>();
        Set<Map<String, Object>> verifiableCredentials = new HashSet<>();

        for(Map<String, Object> claimOrCredential : claimCredentialMix) {
            if(isCredential(claimOrCredential)) {
                verifiableCredentials.add(claimOrCredential);
            } else {
                claims.add(claimOrCredential);
            }
        }
        ClaimCredentialHolder claimCredentialHolder = ClaimCredentialHolder.builder().claims(claims).verifiableCredentials(verifiableCredentials).build();
        log.info(String.format("You provided: %d claims and %d credentials", claimCredentialHolder.getClaims().size(), claimCredentialHolder.getVerifiableCredentials().size()));
        return claimCredentialHolder;
    }

    private boolean isCredential(Map<String, Object> potentialCredential) {
        Object typeValue = potentialCredential.get(TYPEFIELD);
        if(typeValue instanceof Collection) {
            Collection<?> typeCollection = (Collection<?>) typeValue;
            return typeCollection.contains(VERIFIABLECREDENTIAL);
        } else {
            return typeValue.toString().equals(VERIFIABLECREDENTIAL);
        }
    }
}
