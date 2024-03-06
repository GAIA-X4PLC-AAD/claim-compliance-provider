package com.msg.services;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class SdCreatorService {

    @RestClient
    @Inject
    SdCreatorClient sdCreatorApi;

    public Set<Map<String, Object>> transformClaimsToVCs(Set<Map<String, Object>> claims) {
        Set<Map<String, Object>> claimSet = new HashSet<Map<String, Object>>();
        for(Map<String, Object> claimObject : claims) {
            claimSet.add(sdCreatorApi.postClaimsGetVCs(claimObject));
        }
        return claimSet;
    }

    public Map<String, Object> transformVCsToVP(Set<Map<String, Object>> credentials) {
        return sdCreatorApi.postVCsGetVP(credentials);
    }

}