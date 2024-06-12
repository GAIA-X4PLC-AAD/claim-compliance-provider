package com.msg.ccp.controller;

import com.danubetech.verifiablecredentials.VerifiablePresentation;

import java.util.Map;
import java.util.Set;

public interface IClaimComplianceProviderService {
    VerifiablePresentation process(Set<Map<String, Object>> claimCredentialSet);
}
