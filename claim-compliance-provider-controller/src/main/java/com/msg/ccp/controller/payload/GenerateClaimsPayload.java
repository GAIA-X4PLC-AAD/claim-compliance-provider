package com.msg.ccp.controller.payload;

import jakarta.validation.constraints.NotBlank;

public record GenerateClaimsPayload(@NotBlank String identifierPrefix, @NotBlank String legalParticipantId,
                                    @NotBlank String physicalResourceLegalParticipantId) {

    public static final String EXAMPLE_PAYLOAD = """
            {
              "identifierPrefix": "https://www.gaia-x4plcaad.info/claims",
              "legalParticipantId": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json",
              "physicalResourceLegalParticipantId": "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json"
            }
            """;
    public static final String EXAMPLE_RESPONSE = """
            [
              """ + SendClaimsPayload.LIST_OF_CLAIMS  + """
            ]
            """;
}