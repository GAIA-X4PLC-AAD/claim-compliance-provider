package com.msg.ccp.interfaces.controller;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.interfaces.config.IServiceConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IClaimComplianceProviderService extends IServiceConfiguration {
    List<VerifiablePresentation> process(final Set<Map<String, Object>> claims, final Set<VerifiableCredential> verifiableCredentials);
}
