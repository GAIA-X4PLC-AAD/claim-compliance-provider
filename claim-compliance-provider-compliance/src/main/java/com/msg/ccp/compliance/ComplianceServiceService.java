package com.msg.ccp.compliance;

import com.msg.ccp.interfaces.compliance.IComplianceServiceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@ApplicationScoped
@Slf4j
public class ComplianceServiceService implements IComplianceServiceService {

    private final ComplianceServiceClient complianceServiceApi;

    @Inject
    public ComplianceServiceService(@RestClient final ComplianceServiceClient complianceServiceApi) {
        this.complianceServiceApi = complianceServiceApi;
    }

    public Map<String, Object> getComplianceCredential(final Map<String, Object> verifiablePresentationWithoutProof) {
        log.info("call compliance service");
        return this.complianceServiceApi.postVPGetCC(verifiablePresentationWithoutProof);
    }
}
