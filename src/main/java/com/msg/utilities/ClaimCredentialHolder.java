package com.msg.utilities;

import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import com.danubetech.verifiablecredentials.*;

@Getter
@AllArgsConstructor
@Builder
public class ClaimCredentialHolder {
    Set<Map<String, Object>> claims;
    Set<VerifiableCredential> verifiableCredentialsUnordered;
    Set<VerifiableCredential> verifiableCredentialsDomain;
    Set<VerifiableCredential> verifiableCredentialsGX;
}
