package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
public class CredentialContainer {
    Set<VerifiableCredential> verifiableCredentialsDomain;
    Set<VerifiableCredential> verifiableCredentialsGX;
}
