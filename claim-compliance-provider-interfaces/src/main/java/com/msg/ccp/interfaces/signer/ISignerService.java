package com.msg.ccp.interfaces.signer;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.interfaces.config.IServiceConfiguration;

import java.util.Map;
import java.util.Set;

/**
 * Interface for the signer service.
 */
public interface ISignerService extends IServiceConfiguration {

    /**
     * Create verifiable credentials from claims.
     * @param claims the claims to be processed.
     * @return the verifiable credentials.
     */
    Set<VerifiableCredential> createVCsFromClaims(final Set<Map<String, Object>> claims);

    /**
     * Create a verifiable presentation from verifiable credentials.
     * @param credentials the verifiable credentials to be processed.
     * @return the verifiable presentation.
     */
    VerifiablePresentation createVPfromVCs(final Set<VerifiableCredential> credentials);

    /**
     * Create a verifiable presentation without proof from verifiable credentials.
     * @param credentials the verifiable credentials to be processed.
     * @return the verifiable presentation without proof.
     */
    VerifiablePresentation createVPwithoutProofFromVCs(Set<VerifiableCredential> credentials);
}
