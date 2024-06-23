package com.msg.ccp.interfaces.controller;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IClaimComplianceProviderService {
    List<VerifiablePresentation> process(final Set<Map<String, Object>> claims, final Set<VerifiableCredential> verifiableCredentials);
}
