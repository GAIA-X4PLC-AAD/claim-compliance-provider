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
                @Tag(name="Claim Compliance Provider", description = """
                       OpenAPI specification for the Claim Compliance Provider API.
                       This API provides an endpoint for processing Gaia-X participant verifiable credentials and claims
                       in order have Gaia-X compliant and federated catalogue verified and signed claims.
                       In addition to this there is another endpoint to create valid and correctly linked example claims for Gaia-X types.
                       """)

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
