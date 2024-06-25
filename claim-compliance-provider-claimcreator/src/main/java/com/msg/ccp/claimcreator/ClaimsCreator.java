package com.msg.ccp.claimcreator;

import com.msg.ccp.exception.CcpException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class ClaimsCreator {

    private static final List<String> FILE_NAMES = List.of("DataResource-instance.json",
            "InstantiatedVirtualResource-instance.json", "PhysicalResource-instance.json", "ServiceAccessPoint-instance.json",
            "ServiceOffering-instance.json");

    public Set<String> createClaims(final String legalParticipantId, final String physicalResourceLegalParticipantId,
                                   final String identifierPrefix) {
        final Replacement replacement = createReplacement(legalParticipantId, physicalResourceLegalParticipantId, identifierPrefix);

        final Set<String> fileContents = loadFileContents();
        final Set<String> replacedContent = new HashSet<>(fileContents.size());
        for (String content : fileContents) {
            content = content.replace("{dataResourceId}", replacement.dataResourceId());
            content = content.replace("{expirationDateTime}", replacement.expirationDateTime());
            content = content.replace("{instantiatedVirtualResourceId}", replacement.instantiatedVirtualResourceId());
            content = content.replace("{legalParticipantId}", replacement.legalParticipantId());
            content = content.replace("{obsoleteDateTime}", replacement.obsoleteDateTime());
            content = content.replace("{physicalResourceId}", replacement.physicalResourceId());
            content = content.replace("{physicalResourceLegalParticipantId}", replacement.physicalResourceLegalParticipantId());
            content = content.replace("{serviceAccessPointId}", replacement.serviceAccessPointId());
            content = content.replace("{serviceOfferingId}", replacement.serviceOfferingId());
            replacedContent.add(content);
        }
        return replacedContent;
    }

    private Set<String> loadFileContents() {
        final Set<String> claims = new HashSet<>();
        for (final String fileName : FILE_NAMES) {
            try {
                final InputStream inputStream = getInputstream(fileName);
                if (inputStream != null) {
                    final String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    claims.add(content);
                } else {
                    throw new IllegalArgumentException("File not found: " + fileName);
                }
            } catch (final Exception e) {
                throw new CcpException("Error while reading reading template files", e);
            }
        }
        return claims;
    }

    private InputStream getInputstream(final String resource) {
        // this is the path within the jar file in a running app
        InputStream input = getClass().getResourceAsStream("/resources/" + resource);
        if (input == null) {
            // this is how we load file within unit tests
            input = getClass().getClassLoader().getResourceAsStream(resource);
        }
        return input;
    }

    private Replacement createReplacement(final String legalParticipantId, final String physicalResourceLegalParticipantId, final String identifierPrefix) {
        final UUID uuid = UUID.randomUUID();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return new Replacement(identifierPrefix + "/data-resource/" + uuid,
                ZonedDateTime.now(ZoneOffset.UTC).plusMonths(9L).format(dateTimeFormatter),
                identifierPrefix + "/virtual-resource/" + uuid,
                legalParticipantId,
                ZonedDateTime.now(ZoneOffset.UTC).plusMonths(9L).format(dateTimeFormatter),
                identifierPrefix + "/physical-resource/" + uuid,
                physicalResourceLegalParticipantId,
                identifierPrefix + "/service-access-point/" + uuid,
                identifierPrefix + "/service-offering/" + uuid
                );
    }
}
