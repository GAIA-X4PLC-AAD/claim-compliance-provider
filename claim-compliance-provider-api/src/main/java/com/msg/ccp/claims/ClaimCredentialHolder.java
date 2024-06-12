package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
public class ClaimCredentialHolder {
    Set<Map<String, Object>> claims;
    Set<VerifiableCredential> verifiableCredentialsUnordered;
    Set<VerifiableCredential> verifiableCredentialsDomain;
    Set<VerifiableCredential> verifiableCredentialsGX;
}
