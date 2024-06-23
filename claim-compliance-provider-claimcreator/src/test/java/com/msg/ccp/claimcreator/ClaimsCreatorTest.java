package com.msg.ccp.claimcreator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@Slf4j
class ClaimsCreatorTest {

    @Inject
    ClaimsCreator claimsCreator;

    @Test
    @DisplayName("IF claims are created THEN we have the correct amount of claims with replaces placeholders.")
    void testClaimsCreation() throws IOException {
        // prepare
        final String legalParticipantId = "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json";
        final String physicalResourceLegalParticipantId = "https://participant.gxfs.gx4fm.org/msg-systems-ag/legalParticipant.json";
        final String identifierPrefix = "https://www.gaia-x4plcaad.info/claims";

        // action
        final Set<String> claims = claimsCreator.createClaims(legalParticipantId, physicalResourceLegalParticipantId,
                identifierPrefix);
        printClaims(claims);

        // assert
        assertThat(claims).hasSize(countFiles());
        for (final String claim : claims) {
            assertThat(claim)
                    .doesNotContain("{serviceOfferingId}")
                    .doesNotContain("{dataResourceId}")
                    .doesNotContain("{serviceAccessPointId}")
                    .doesNotContain("{legalParticipantId}")
                    .doesNotContain("{obsoleteDateTime}")
                    .doesNotContain("{expirationDateTime}")
                    .doesNotContain("{instantiatedVirtualResourceId}")
                    .doesNotContain("{physicalResourceId}")
                    .doesNotContain("{physicalResourceLegalParticipantId}");
        }
    }

    /**
        * Print the claims for having them as an example payload.
     * @param claims claims to be printed
     */
    private void printClaims(final Set<String> claims) throws JsonProcessingException {
        final ObjectMapper objectMapper  = new ObjectMapper();
        final JsonNode jsonNode = objectMapper.readTree(String.valueOf(claims));
        log.info("Claims: {}",  jsonNode);
    }
    private int countFiles() throws IOException {
        try (final Stream<Path> files = Files.list(Paths.get("src/main/resources"))) {
            return (int) files.count();
        }
    }
}