package com.msg.ccp.sdcreator;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@ApplicationScoped
public class SdCreatorService implements ISignerService {

    private final SdCreatorClient sdCreatorClient;

    @Inject
    public SdCreatorService(@RestClient final SdCreatorClient sdCreatorClient) {
        this.sdCreatorClient = sdCreatorClient;
    }

    public Set<VerifiableCredential> createVCsFromClaims(final Set<Map<String, Object>> claims) {
        final Set<VerifiableCredential> vCSet = new HashSet<>();
        for(final Map<String, Object> claimObject : claims) {
            vCSet.add(VerifiableCredential.fromMap(sdCreatorClient.postClaimsGetVCs(claimObject)));
        }
        return vCSet;
    }

    public VerifiablePresentation createVPfromVCs(final Set<VerifiableCredential> credentials) {
        return VerifiablePresentation.fromMap(sdCreatorClient.postVCsGetVP(credentials));
    }

    public Map<String, Object> createVPwithoutProofFromVCs(final Set<Map<String, Object>> credentials) {
        return sdCreatorClient.wrapCredentialsIntoVerifiablePresentationWithoutProof(credentials);
    }
}