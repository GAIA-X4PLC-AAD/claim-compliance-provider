package com.msg.ccp.controller;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.msg.ccp.claimcreator.ClaimsCreator;
import com.msg.ccp.controller.payload.GenerateClaimsPayload;
import com.msg.ccp.controller.payload.SendClaimsPayload;
import com.msg.ccp.exception.CcpException;
import com.msg.ccp.exception.ErrorResponse;
import com.msg.ccp.interfaces.controller.IClaimComplianceProviderService;
import com.msg.ccp.util.VpVcUtil;
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

import java.util.List;
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
    @Operation(summary = "Initiate VC Processing", description = """
            This operation initiates the processing of **Gaia-X participant credentials** and claims.
            The input payload contains `claims` and `verifiableCredentials`.
            * The `claims` are the data that the participant wants to sign and process with this service.
            * The `verifiableCredentials` are the participant credentials that are sent along the signed claims to the __Gaia-X compliance service__.
            
            The claims
            * will be signed and
            * checked against a __Gaia-X compliance service__ and
            * sent to a __federated catalogue instance__ (for either checking the verifiable presentation or storing it directly (depending on the implementation).
            
            As a result of the processing, 3 verifiable presentation are created and returned to the caller as a list.
            This verifiable presentations are split into 3 types of verifiable credentials:
            * VP #1 with ServiceOffering
            * VP #2 with everything else than ServiceOffering, LegalParticipant, LegalRegistrationNumber, GaiaXTermsAndConditions
            * VP #3 with compliance credential.
            
            Note that only VPs #1 and #2 are sent to the federated catalogue.
            All VPs are signed by the signing implementation and can be used by the participant to prove the claims to other services.
            
            >**Notes / hints**
            >* The ID of the legalParticipant `legalParticipant` credential should match the ID of participant links (e.g. `providedBy`) in the claims.
            >* If there is a domain specific type in the list of `claims` the ID of that type must match thd ID of the Gaia-X superclass (e.g. `DataResource`) in the claims.
            """)
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
    public List<Map<String, Object>> initiateVCProcessing(final SendClaimsPayload payload) {
        log.info("Initiating VC processing");
        log.debug("SendClaimsPayload: {}", payload);
        final List<VerifiablePresentation> result = verifiableCredentialsProcessor.process(payload.claims(), payload.verifiableCredentials());
        log.info("VC processing completed. VPs: {}", result.stream().map(VpVcUtil::getId).toList());

        log.debug("Result: {}", result);
        return result.stream()
                .map(VerifiablePresentation::toMap)
                .toList();
    }

    @POST
    @Path("/v1/generate-claims")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Generate Gaia-X claims", description = """
            This operation **generates** claims based on the provided parameters.
            The list of entities returned contains
            * a `ServiceOffering`,
            * a `DataResource`,
            * a `InstantiatedVirtualResource`,
            * a `PhysicalResource` and
            * a `ServiceAccessPoint`.
            """)
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
    public JsonNode generateClaims(final GenerateClaimsPayload payload) {
        log.info("Generating Gaia-X claims");
        log.debug("GenerateClaimsPayload: {}", payload);
        final Set<String> claims = claimsCreator.createClaims(payload.legalParticipantId(), payload.physicalResourceLegalParticipantId(),
                payload.identifierPrefix());
        log.info("Gaia-X claims generated");
        log.debug("Claims: {}", claims);
        return convertToJson(claims);
    }

    private JsonNode convertToJson(final Set<String> claims) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.readTree(String.valueOf(claims));
        } catch (final JsonProcessingException e) {
            throw new CcpException("Error parsing the generated json strings back to json object.", e);
        }
    }
}