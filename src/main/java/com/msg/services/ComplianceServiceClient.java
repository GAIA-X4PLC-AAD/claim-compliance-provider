package com.msg.services;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterRestClient
public interface ComplianceServiceClient {
    
    @POST
    @Path("/examplepath")
    @Produces("application/json")
    Set<HashMap<String, Object>> postVCsGetCCs(Set<HashMap<String, Object>> credentials);
}