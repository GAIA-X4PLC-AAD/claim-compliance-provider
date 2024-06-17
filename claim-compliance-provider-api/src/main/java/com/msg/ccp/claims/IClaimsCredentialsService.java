package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiableCredential;

import java.util.Set;

public interface IClaimsCredentialsService {
    CredentialContainer separateDomainSpecificCredentials(final Set<VerifiableCredential> verifiableCredentials);
}
