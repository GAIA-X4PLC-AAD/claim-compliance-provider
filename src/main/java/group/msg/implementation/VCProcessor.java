package group.msg.implementation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class VCProcessor {

    private ComplianceServiceService complianceService;
    private SdCreatorService sdCreator;

    @Inject
    public VCProcessor(ComplianceServiceService complianceService, SdCreatorService sdCreator) {
        this.complianceService = complianceService;
        this.sdCreator = sdCreator;
    }

    public Set<HashMap<String, Object>> transformClaimsToVCs(Set<HashMap<String, Object>> claims) {
        return this.sdCreator.transformClaimsToVCs(claims);
    }

    public Set<HashMap<String, Object>> getComplianceCredentials(Set<HashMap<String, Object>> verifiableCredentials) {
        return this.complianceService.getComplianceCredentials(verifiableCredentials);
    }

    public HashMap<String, Object> mergeVCAndCC(Set<HashMap<String, Object>> verifiableCredentials, Set<HashMap<String, Object>> complianceCredentials) {
        Set<HashMap<String, Object>> mergedCredentials = new HashSet<>();
        mergedCredentials.addAll(verifiableCredentials);
        mergedCredentials.addAll(complianceCredentials);
        return this.sdCreator.transformVCsToVP(mergedCredentials);
    }
}