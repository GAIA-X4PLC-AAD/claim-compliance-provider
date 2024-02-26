package com.msg.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class SdCreatorService {

    @RestClient
    @Inject
    private SdCreatorClient sdCreatorApi;
    
    @Inject
    public SdCreatorService() {
    }

    public Set<HashMap<String, Object>> transformClaimsToVCs(Set<HashMap<String, Object>> claims) {
        Set<HashMap<String, Object>> claimSet = new HashSet<HashMap<String, Object>>();
        for(HashMap<String, Object> claimObject : claims) {
            claimSet.add(sdCreatorApi.postClaimsGetVCs(claimObject));
        }
        return claimSet;
    }

    public HashMap<String, Object> transformVCsToVP(Set<HashMap<String, Object>> credentials) {
        return sdCreatorApi.postVCsGetVP(credentials);
    }

}