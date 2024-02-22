package group.msg.implementation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.rest.client.inject.RestClient;

// import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ComplianceServiceService {

    @RestClient
    @Inject
    private ComplianceServiceClient complianceServiceApi;

    @Inject
    public ComplianceServiceService() {
    }

    public Set<HashMap<String, Object>> getComplianceCredentials(Set<HashMap<String, Object>> verifiableCredentials) {
        return new HashSet<HashMap<String, Object>>(); // is getting replaced as soon as conenction to Compliance Service is enabled
        // return this.complianceServiceApi.postVCsGetCCs(verifiableCredentials);
    }
}
