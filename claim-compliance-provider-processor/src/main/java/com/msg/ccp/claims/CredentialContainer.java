package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CredentialContainer(@NotNull Set<VerifiableCredential> verifiableCredentialsDomain, @NotNull Set<VerifiableCredential> verifiableCredentialsGX) {
}
