package com.msg.ccp.compliance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@ApplicationScoped
public class ComplianceServiceService implements IComplianceServiceService {

    private final ComplianceServiceClient complianceServiceApi;

    @Inject
    public ComplianceServiceService(@RestClient final ComplianceServiceClient complianceServiceApi) {
        this.complianceServiceApi = complianceServiceApi;
    }

    public Map<String, Object> getComplianceCredential(Map<String, Object> verifiablePresentationWithoutProof) {
        return this.complianceServiceApi.postVPGetCC(verifiablePresentationWithoutProof);
    }
}
