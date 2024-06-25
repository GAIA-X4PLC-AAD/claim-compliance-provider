package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class ClaimsCredentialsServiceTest {

    @Inject
    ClaimsCredentialsService claimsCredentialsService;

    @Test
    @DisplayName("IF credential subject ids of domain specific subjects are not found in gx subjects THEN the list of non existent subjects is returned.")
    void testUnmappedCredentialSubjectIds() {
        // prepare
        final List<String> domainSpecificIds = Arrays.asList("1", "2", "3", "4", "5");
        final List<String> gxIds = Arrays.asList("1", "2", "3");
        final CredentialContainer credentialContainer = createCredentialContainer(domainSpecificIds, gxIds);
        final Set<String> expectedUnmappedCredentialSubjectIds = Set.of("4", "5");

        // action
        final List<String> foundIds = claimsCredentialsService.
                getDomainSpecificCredentialSubjectIdsNotExistentInGxCredentialSubjectIds(credentialContainer);

        // test
        assertThat(foundIds).containsExactlyInAnyOrderElementsOf(expectedUnmappedCredentialSubjectIds);
    }

    @Test
    @DisplayName("IF credential subject ids of domain specific subjects is empty THEN an empty List is returned.")
    void testNoDomainSpecificCredentialSubjectIds() {
        // prepare
        final List<String> domainSpecificIds = List.of();
        final List<String> gxIds = Arrays.asList("1", "2", "3");
        final CredentialContainer credentialContainer = createCredentialContainer(domainSpecificIds, gxIds);
        final Set<String> expectedUnmappedCredentialSubjectIds = Set.of();

        // action
        final List<String> foundIds = claimsCredentialsService.
                getDomainSpecificCredentialSubjectIdsNotExistentInGxCredentialSubjectIds(credentialContainer);

        // test
        assertThat(foundIds).containsExactlyInAnyOrderElementsOf(expectedUnmappedCredentialSubjectIds);
    }

    private CredentialContainer createCredentialContainer(final List<String> domainSpecificIds, final List<String> gxIds) {
        final Set<VerifiableCredential> domainSpecificVCs = new HashSet<>();
        for (final String id : domainSpecificIds) {
            domainSpecificVCs.add(createVerifiableCredential(id));
        }

        final Set<VerifiableCredential> gxVCs = new HashSet<>();
        for (final String id : gxIds) {
            gxVCs.add(createVerifiableCredential(id));
        }

        return new CredentialContainer(domainSpecificVCs, gxVCs);
    }

    private VerifiableCredential createVerifiableCredential(final String id) {
        final Map<String, Object> credentialSubject = new HashMap<>();
        credentialSubject.put("id", id);
        final Map<String, Object> verifiableCredential = new HashMap<>();
        verifiableCredential.put("credentialSubject", credentialSubject);
        return VerifiableCredential.fromMap(verifiableCredential);
    }
}
