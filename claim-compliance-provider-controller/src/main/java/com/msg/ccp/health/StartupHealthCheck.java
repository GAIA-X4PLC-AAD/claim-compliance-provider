package com.msg.ccp.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.health.*;


@Startup
@ApplicationScoped
public class StartupHealthCheck implements HealthCheck {

    @Produces
    @Startup
    @ApplicationScoped
    public HealthCheck startup() {
        return () -> HealthCheckResponse.named("Startup check").up().build();
    }

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("StartupHealthCheck");
        // Implement your logic to check if the application is started
        // If the application is started, return up(). Otherwise, return down().
        return responseBuilder.up().build();
    }
}