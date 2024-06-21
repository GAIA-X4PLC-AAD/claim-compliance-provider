package com.msg.ccp.claimcreator;

import com.msg.ccp.exception.CcpException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@ApplicationScoped
@Slf4j
public class ClaimsCreator {

    public Set<String> createClaims(final String legalParticipantId, final String physicalResourceLegalParticipantId,
                                    final String registrationNumber, final String countryCode, final String identifierPrefix) {
        Replacement replacement = createReplacement(legalParticipantId, physicalResourceLegalParticipantId, registrationNumber, countryCode, identifierPrefix);
        final Set<String> claims = new HashSet<>(5);
        try (final Stream<Path> paths = Files.walk(Paths.get("src/main/resources"))) {
            paths.filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            String content = new String(Files.readAllBytes(file));
                            content = content.replace("{countrySubdivisionCode}", replacement.getCountrySubdivisionCode());
                            content = content.replace("{dataResourceId}", replacement.getDataResourceId());
                            content = content.replace("{expirationDateTime}", replacement.getExpirationDateTime());
                            content = content.replace("{instantiatedVirtualResourceId}", replacement.getInstantiatedVirtualResourceId());
                            content = content.replace("{legalParticipantId}", replacement.getLegalParticipantId());
                            content = content.replace("{legalRegistrationNumber}", replacement.getLegalRegistrationNumber());
                            content = content.replace("{obsoleteDateTime}", replacement.getObsoleteDateTime());
                            content = content.replace("{physicalResourceId}", replacement.getPhysicalResourceId());
                            content = content.replace("{physicalResourceLegalParticipantId}", replacement.getPhysicalResourceLegalParticipantId());
                            content = content.replace("{serviceAccessPointId}", replacement.getServiceAccessPointId());
                            content = content.replace("{serviceOfferingId}", replacement.getServiceOfferingId());
                            claims.add(content);
                        } catch (final IOException e) {
                            throw new CcpException("Error while reading reading template file " + file, e);
                        }
                    });
        } catch (final IOException e) {
            throw new CcpException("Error walking the file tree in src/main/resources", e);
        }
        return claims;
    }

    private Replacement createReplacement(final String legalParticipantId, final String physicalResourceLegalParticipantId, final String registrationNumber, final String countryCode, final String identifierPrefix) {
        final Replacement replacement = new Replacement();
        final UUID uuid = UUID.randomUUID();
        replacement.setCountrySubdivisionCode(countryCode);
        replacement.setDataResourceId(identifierPrefix + "/data-resource/" + uuid);
        replacement.setExpirationDateTime(ZonedDateTime.now(ZoneOffset.UTC).plusMonths(9L).format(DateTimeFormatter.ISO_DATE_TIME));
        replacement.setInstantiatedVirtualResourceId(identifierPrefix + "/virtual-resource/" + uuid);
        replacement.setLegalParticipantId(legalParticipantId);
        replacement.setLegalRegistrationNumber(registrationNumber);
        replacement.setObsoleteDateTime(ZonedDateTime.now(ZoneOffset.UTC).plusMonths(9L).format(DateTimeFormatter.ISO_DATE_TIME));
        replacement.setPhysicalResourceId(identifierPrefix + "/physical-resource/" + uuid);
        replacement.setPhysicalResourceLegalParticipantId(physicalResourceLegalParticipantId);
        replacement.setServiceAccessPointId(identifierPrefix + "/service-access-point/" + uuid);
        replacement.setServiceOfferingId(identifierPrefix + "/service-offering/" + uuid);
        return replacement;
    }
}
