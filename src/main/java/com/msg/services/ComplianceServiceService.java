package com.msg.services;

import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ComplianceServiceService {

    @RestClient
    @Inject
    ComplianceServiceClient complianceServiceApi;

    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
            return new RuntimeException("The Compliance Service responded with HTTP 409");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            return new RuntimeException("The Compliance Service responded with HTTP 404");
        }
        return new RuntimeException(String.format("Unknown issue calling Compliance Service, response is %s", response.getStatusInfo().getReasonPhrase()));
    }

    public Map<String, Object> getComplianceCredential(Map<String, Object> verifiablePresentationWithoutProof) {
        return this.complianceServiceApi.postVPGetCC(verifiablePresentationWithoutProof);
    }
}
