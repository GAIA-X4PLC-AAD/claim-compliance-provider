package com.msg.ccp.interfaces.compliance;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.interfaces.config.IServiceConfiguration;

/**
 * Interface for the compliance service.
 */
public interface IComplianceServiceService extends IServiceConfiguration {
    /**
     * Call the compliance service and get the compliance credential.
     * @param verifiablePresentationWithoutProof the verifiable presentation without proof.
     * @return the compliance credential.
     */
    VerifiableCredential getComplianceCredential(final VerifiablePresentation verifiablePresentationWithoutProof);
}
