package com.msg.services;

import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ComplianceServiceService {

    @RestClient
    @Inject
    ComplianceServiceClient complianceServiceApi;

    public Map<String, Object> getComplianceCredential(Map<String, Object> verifiablePresentationWithoutProof) {
        return this.complianceServiceApi.postVPGetCC(verifiablePresentationWithoutProof);
    }
}
