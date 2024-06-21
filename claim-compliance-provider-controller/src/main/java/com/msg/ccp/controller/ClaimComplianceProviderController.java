package com.msg.ccp.controller;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.msg.ccp.claimcreator.ClaimsCreator;
import com.msg.ccp.controller.payload.GenerateClaimsPayload;
import com.msg.ccp.controller.payload.SendClaimsPayload;
import com.msg.ccp.exception.ErrorResponse;
import com.msg.ccp.interfaces.controller.IClaimComplianceProviderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.Map;
import java.util.Set;

@ApplicationScoped
@Slf4j
@Path("")
public class ClaimComplianceProviderController {

    private final IClaimComplianceProviderService verifiableCredentialsProcessor;
    private final ClaimsCreator claimsCreator;

    @Inject
    public ClaimComplianceProviderController(final IClaimComplianceProviderService verifiableCredentialsProcessor, final ClaimsCreator claimsCreator) {
        this.verifiableCredentialsProcessor = verifiableCredentialsProcessor;
        this.claimsCreator = claimsCreator;
    }

    @POST
    @Path("/v1/send-claims")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Initiate VC Processing", description = "This operation initiates the processing of Gaia-X participant credentials and claims. " +
            "The claims will be signed and checked against a Gaia-X compliance service. In addition to that a federated catalogue instance is called " +
            "(for either checking the verifiable presentation or storing it (depending on the implementation). As a result of the processing, a list of " +
            "verifiable presentations is created and returned to the caller. The input payload contains claims and verifiable credentials. The claims are " +
            "the data that the participant wants to sign and check with the service. " +
            "The verifiable credentials are the participant credentials that are sent along the signed claims to the Gaia-X compliance service. " +
            "The result verifiable presentations are signed by the signing implementation and can be used by the participant to prove the claims " +
            "to other services.")
    @RequestBody(description = "Payload containing claims and verifiable credentials", required = true,
            content = @Content(schema = @Schema(implementation = SendClaimsPayload.class), examples = {
                    @ExampleObject(name = "examplePayload", value = SendClaimsPayload.EXAMPLE_PAYLOAD)
            }))
    @APIResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @APIResponse(responseCode = "409", description = "Conflict",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "exampleErrorResponse409", value = SendClaimsPayload.EXAMPLE_RESPONSE_409)
            }))
    @APIResponse(responseCode = "422", description = "Unprocessable Entity",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "exampleErrorResponse422", value = SendClaimsPayload.EXAMPLE_RESPONSE_422)
            }))
    @APIResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "exampleErrorResponse500", value = SendClaimsPayload.EXAMPLE_RESPONSE_500)
            }))
    public Map<String, Object> initiateVCProcessing(final SendClaimsPayload payload) {
        log.info("Initiating VC processing");
        log.debug("SendClaimsPayload: {}", payload);
        final VerifiablePresentation result = verifiableCredentialsProcessor.process(payload.claims(), payload.verifiableCredentials());
        log.info("VC processing completed");
        log.debug("Result: {}", result);
        return result.toMap();
    }

    @POST
    @Path("/v1/generate-claims")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Generate Gaia-X claims", description = "This operation generates claims based on the provided parameters. " +
            "The list of entities returned is a ServiceOffering, DataResource, InstantiatedVirtualResource, PhysicalResource " +
            "and ServiceAccessPoint.")
    @RequestBody(description = "Claim input parameters", required = true,
            content = @Content(schema = @Schema(implementation = GenerateClaimsPayload.class), examples = {
                    @ExampleObject(name = "examplePayload", value = GenerateClaimsPayload.EXAMPLE_PAYLOAD)
            }))
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = Set.class),
                    examples = {
                            @ExampleObject(name = "exampleClaims", value = GenerateClaimsPayload.EXAMPLE_RESPONSE
                            )
                    })),
            @APIResponse(responseCode = "400", description = "Invalid or missing parameters"),
            @APIResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "exampleErrorResponse500", value = SendClaimsPayload.EXAMPLE_RESPONSE_500)
                    }))
    })
    public Set<String> generateClaims(final GenerateClaimsPayload payload) {
        log.info("Generating Gaia-X claims");
        log.debug("GenerateClaimsPayload: {}", payload);
        final Set<String> claims = claimsCreator.createClaims(payload.legalParticipantId(), payload.physicalResourceLegalParticipantId(),
                payload.registrationNumber(), payload.countryCode(), payload.identifierPrefix());
        log.info("Gaia-X claims generated");
        log.debug("Claims: {}", claims);
        return claims;
    }
}