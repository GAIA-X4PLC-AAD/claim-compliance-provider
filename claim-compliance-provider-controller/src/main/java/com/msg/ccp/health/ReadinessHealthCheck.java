package com.msg.ccp.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;


@Readiness
@ApplicationScoped
public class ReadinessHealthCheck implements HealthCheck {

    @Produces
    @Readiness
    @ApplicationScoped
    public HealthCheck readiness() {
        return () -> HealthCheckResponse.named("Readiness check").up().build();
    }

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("ReadinessHealthCheck");
        // Implement your logic to check if the application is ready
        // If the application is ready, return up(). Otherwise, return down().
        return responseBuilder.up().build();
    }
}