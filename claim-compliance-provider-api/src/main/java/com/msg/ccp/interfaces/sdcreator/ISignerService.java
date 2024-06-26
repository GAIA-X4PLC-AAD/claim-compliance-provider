package com.msg.ccp.interfaces.sdcreator;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.interfaces.config.IServiceConfiguration;

import java.util.Map;
import java.util.Set;

public interface ISignerService extends IServiceConfiguration {

    Set<VerifiableCredential> createVCsFromClaims(final Set<Map<String, Object>> claims);

    VerifiablePresentation createVPfromVCs(final Set<VerifiableCredential> credentials);

    Map<String, Object> createVPwithoutProofFromVCs(final Set<Map<String, Object>> credentials);
}
