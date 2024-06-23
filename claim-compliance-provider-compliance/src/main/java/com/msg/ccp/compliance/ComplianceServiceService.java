package com.msg.ccp.compliance;

import com.msg.ccp.exception.RestClientException;
import com.msg.ccp.interfaces.compliance.IComplianceServiceService;
import com.msg.ccp.util.VpVcUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
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
        try {
            return this.complianceServiceApi.postVPGetCC(verifiablePresentationWithoutProof);
        } catch (final WebApplicationException e) {
            final Response response = e.getResponse();
            if (response.hasEntity()) {
                final Map<String, Object> errorDetails = response.readEntity(Map.class);
                throw new RestClientException(
                        createMessage(errorDetails),
                        (String) errorDetails.get("error"),
                        e.getMessage(),
                        response.getStatus(),
                        VpVcUtil.getId(verifiablePresentationWithoutProof)
                );
            } else {
                throw new RestClientException(
                        "An error occurred while calling the compliance service",
                        "Unknown error",
                        e.getMessage(),
                        response.getStatus(),
                        VpVcUtil.getId(verifiablePresentationWithoutProof)
                );
            }
        }
    }

    private static String createMessage(final Map<String, Object> errorDetails) {
        if (errorDetails.containsKey("message")) {
            // message might be a single string or a list of strings
            return errorDetails.get("message").toString();
        } else {
            return "no message from compliance service";
        }
    }
}
