package com.msg.ccp;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.ExternalDocumentation;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name="Claim compliance provider", description="OpenAPI specification for the Claim Compliance Provider API. " +
                        "This API provides a service for processing Gaia-X participant verifiable credentials and claims. " +
                        "The claims will be signed and checked against a Gaia-X compliance service. In addition to that " +
                        "a federated catalogue instance is called (for either checking the verifiable presentation or storing it " +
                        "(depending on the implementation). " +
                        "As a result of the processing, a list of verifiable presentations is created and returned to the caller.")
        },
        info = @Info(
                title="Claim Compliance Provider API",
                version = "1.0.0",
                contact = @Contact(
                        name = "Claim Compliance Provider API Support",
                        url = "https://www.gaia-x4plcaad.info/",
                        email = "TP2@gaia-x4plcaad.info"),
                license = @License(
                        name = "EPL 2.0",
                        url = "https://www.eclipse.org/legal/epl-2.0/")),
        externalDocs = @ExternalDocumentation(description = "GitHub Repository", url = "https://github.com/GAIA-X4PLC-AAD/claim-compliance-provider")
)
public class APIApplication extends Application {
}
