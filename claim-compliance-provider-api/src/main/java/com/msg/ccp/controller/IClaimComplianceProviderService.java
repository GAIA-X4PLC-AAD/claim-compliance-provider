package com.msg.ccp.controller;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;

import java.util.Map;
import java.util.Set;

public interface IClaimComplianceProviderService {
    VerifiablePresentation process(final Set<Map<String, Object>> claims, final Set<VerifiableCredential> verifiableCredentials);
}
