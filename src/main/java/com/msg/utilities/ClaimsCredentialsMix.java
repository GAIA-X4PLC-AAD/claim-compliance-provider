package com.msg.utilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ClaimsCredentialsMix {

    Set<HashMap<String, Object>> claims;
    Set<HashMap<String, Object>> verifiableCredentials;
    
    @Inject
    public ClaimsCredentialsMix(Set<HashMap<String, Object>> claims, Set<HashMap<String, Object>> credentials) {
        this.claims = claims;
        this.verifiableCredentials = credentials;
    }

    public ClaimsCredentialsMix createClaimsCredentialsMix(Set<HashMap<String, Object>> claimCredentialMix) {
        for(HashMap<String, Object> claimOrCredential : claimCredentialMix) {
            if(isCredential(claimOrCredential)) {
                this.verifiableCredentials.add(claimOrCredential);
            } else {
                this.claims.add(claimOrCredential);
            }
        }
        System.out.println(String.format("You provided: %d claims and %d credentials", this.claims.size(), this.verifiableCredentials.size()));
        return this;
    }

    private boolean isCredential(HashMap<String, Object> potentialCredential) {
        Object typeValue = potentialCredential.get("type");
        if(typeValue instanceof Collection) {
            Collection<?> typeCollection = (Collection<?>) typeValue;
            return typeCollection.contains("VerifiableCredential");
        } else {
            return typeValue.toString() == "VerifiableCredential";
        }
    }

    public Set<HashMap<String, Object>> getCredentials() {
        return this.verifiableCredentials;
    }

    public Set<HashMap<String, Object>> getClaims() {
        return this.claims;
    }
}
