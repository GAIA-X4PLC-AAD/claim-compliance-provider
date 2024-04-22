package com.msg.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@ApplicationScoped
public class SdCreatorService {

    @RestClient
    @Inject
    SdCreatorClient sdCreatorClient;

    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            return new RuntimeException("The Self Description Creator responded with HTTP 404");
        }
        return new RuntimeException(String.format("Unknown issue calling Self Description Creator, response is %s", response.getStatusInfo().getReasonPhrase()));
    }

    public Set<VerifiableCredential> transformClaimsToVCs(Set<Map<String, Object>> claims) {
        Set<VerifiableCredential> vCSet = new HashSet<>();
        for(Map<String, Object> claimObject : claims) {
            vCSet.add(VerifiableCredential.fromMap(sdCreatorClient.postClaimsGetVCs(claimObject)));
        }
        return vCSet;
    }

    public VerifiablePresentation transformVCsToVP(Set<VerifiableCredential> credentials) {
        return VerifiablePresentation.fromMap(sdCreatorClient.postVCsGetVP(credentials));
    }

    public Map<String, Object> wrapCredentialsIntoVerifiablePresentationWithoutProof(Set<Map<String, Object>> credentials) {
        return sdCreatorClient.wrapCredentialsIntoVerifiablePresentationWithoutProof(credentials);
    }


}