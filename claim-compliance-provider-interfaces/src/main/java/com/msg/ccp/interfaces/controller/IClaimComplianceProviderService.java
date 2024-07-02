package com.msg.ccp.interfaces.controller;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.interfaces.config.IServiceConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for the claim compliance provider service. The workflow should be implemented in the process method.
 */
public interface IClaimComplianceProviderService extends IServiceConfiguration {
    /**
     * Process the claims and verifiable credentials and return the verifiable presentations.
     * @param claims the claims to be processed.
     * @param verifiableCredentials the verifiable credentials to be processed.
     * @return the verifiable presentations.
     */
    List<VerifiablePresentation> process(final Set<Map<String, Object>> claims, final Set<VerifiableCredential> verifiableCredentials);
}
