package com.msg.services;

import java.util.Map;
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
    @Path("/examplepath") // kann dies in eine Config Variable in der application.properties ausgelagert werden?
    @Produces("application/json")
    Set<Map<String, Object>> postVCsGetCCs(Set<Map<String, Object>> credentials);
}