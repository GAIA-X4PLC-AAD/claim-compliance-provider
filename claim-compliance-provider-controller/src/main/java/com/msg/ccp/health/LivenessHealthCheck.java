package com.msg.ccp.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.health.*;


@Liveness
@ApplicationScoped
public class LivenessHealthCheck implements HealthCheck {

    @Produces
    @Liveness
    @ApplicationScoped
    public HealthCheck liveness() {
        return () -> HealthCheckResponse.named("Liveness check").up().build();
    }

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("LivenessHealthCheck");
        // Implement your logic to check if the application is live
        // If the application is live, return up(). Otherwise, return down().
        return responseBuilder.up().build();
    }
}