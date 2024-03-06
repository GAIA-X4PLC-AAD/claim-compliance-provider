package com.msg.services;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ComplianceServiceService {

    @RestClient
    @Inject
    ComplianceServiceClient complianceServiceApi;

    public Set<Map<String, Object>> getComplianceCredentials(Set<Map<String, Object>> verifiableCredentials) {
        return new HashSet<Map<String, Object>>(); // is getting replaced as soon as conenction to Compliance Service is enabled
        // return this.complianceServiceApi.postVCsGetCCs(verifiableCredentials);
    }
}
