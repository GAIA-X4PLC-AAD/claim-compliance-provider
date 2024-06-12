package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiableCredential;

import java.util.Map;
import java.util.Set;

public interface IClaimsCredentialsService {
    ClaimCredentialHolder createClaimCredentialHolder(final Set<Map<String, Object>> claimCredentialSet);

    ClaimCredentialHolder separateDomainSpecificCredentials(final Set<VerifiableCredential> verifiableCredentials);
}
