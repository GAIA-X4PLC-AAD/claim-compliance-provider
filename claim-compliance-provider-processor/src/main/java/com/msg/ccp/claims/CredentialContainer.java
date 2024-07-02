package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CredentialContainer(Set<VerifiableCredential> verifiableCredentialsDomain, @NotNull @NotEmpty Set<VerifiableCredential> verifiableCredentialsGX) {
}
