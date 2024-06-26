package com.msg.ccp.sdcreator;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.exception.RestClientException;
import com.msg.ccp.interfaces.sdcreator.ISignerService;
import com.msg.ccp.util.VpVcUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


@ApplicationScoped
@Slf4j
public class SdCreatorService implements ISignerService {

    public static final String SD_CREATOR_ERROR = "An error occurred while calling the sd creator: ";
    public static final String UNKNOWN_ERROR = "Unknown error";
    private final SdCreatorClient sdCreatorClient;

    @Inject
    public SdCreatorService(@RestClient final SdCreatorClient sdCreatorClient) {
        this.sdCreatorClient = sdCreatorClient;
    }

    public Set<VerifiableCredential> createVCsFromClaims(final Set<Map<String, Object>> claims) {
        log.info("call sd-creator: createVCsFromClaims");
        final Set<VerifiableCredential> vCSet = new HashSet<>();
        for(final Map<String, Object> claimObject : claims) {
            try {
                vCSet.add(VerifiableCredential.fromMap(sdCreatorClient.postClaimsGetVCs(claimObject)));
            } catch (final WebApplicationException e) {
                final Response response = e.getResponse();
                throw new RestClientException(
                        SD_CREATOR_ERROR + "createVCsFromClaims",
                        UNKNOWN_ERROR,
                        e.getMessage(),
                        response.getStatus(),
                        VpVcUtil.getId(claimObject)
                );
            }
        }
        return vCSet;
    }

    public Set<Map<String, Object>> getConfig() {
        Set<Map<String, Object>> configs = new HashSet<>();
        Map<String, Object> property = new LinkedHashMap<>();
        property.put(KEY_PROPERTY, "SD_CREATOR_URL");
        property.put(VALUE_PROPERTY, ConfigProvider.getConfig().getValue("quarkus.rest-client.\"com.msg.ccp.sdcreator.SdCreatorClient\".url", String.class));
        configs.add(property);
        return configs;

    }

    public VerifiablePresentation createVPfromVCs(final Set<VerifiableCredential> credentials) {
        log.info("call sd-creator: createVPfromVCs");
        try {
            return VerifiablePresentation.fromMap(sdCreatorClient.postVCsGetVP(credentials));
        } catch (final WebApplicationException e) {
            final Response response = e.getResponse();
            throw new RestClientException(
                    SD_CREATOR_ERROR + "createVPfromVCs",
                    UNKNOWN_ERROR,
                    e.getMessage(),
                    response.getStatus(),
                    "unknown"
            );
        }
    }

    public Map<String, Object> createVPwithoutProofFromVCs(final Set<Map<String, Object>> credentials) {
        log.info("call sd-creator: createVPwithoutProofFromVCs");
        try {
        return sdCreatorClient.wrapCredentialsIntoVerifiablePresentationWithoutProof(credentials);
        } catch (final WebApplicationException e) {
            final Response response = e.getResponse();
            throw new RestClientException(
                    SD_CREATOR_ERROR + "createVPwithoutProofFromVCs",
                    UNKNOWN_ERROR,
                    e.getMessage(),
                    response.getStatus(),
                    "unknown"
            );
        }
    }
}