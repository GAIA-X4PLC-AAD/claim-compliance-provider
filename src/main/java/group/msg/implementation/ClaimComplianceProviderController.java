package group.msg.implementation;

import java.util.HashMap;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("")
public class ClaimComplianceProviderController {

    private VCProcessor vcProcess;

    @Inject
    public ClaimComplianceProviderController(VCProcessor vcProcess) {
        this.vcProcess = vcProcess;
    }

    @POST
    @Path("/send-claims")
    @Consumes("application/json")
    public HashMap<String, Object> initiateVCProcessing(Set<HashMap<String, Object>> claims) {
        Set<HashMap<String, Object>> credentials = vcProcess.transformClaimsToVCs(claims);
        Set<HashMap<String, Object>> complianceCredentials = vcProcess.getComplianceCredentials(credentials);
        HashMap<String, Object> verifiablePresentation = vcProcess.mergeVCAndCC(credentials, complianceCredentials);
        return verifiablePresentation;
    }
}