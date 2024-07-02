package com.msg.ccp.claims;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.msg.ccp.util.VpVcUtil;
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

    @Test
    @DisplayName("IF ServiceOffering is existent THEN ServiceOffering is returned")
    void testFindServiceOfferingsWithMatch() {
        // Prepare
        final Set<VerifiableCredential> verifiableCredentials = new HashSet<>();
        verifiableCredentials.add(createVerifiableCredential("1", List.of("gx:ServiceOffering")));
        verifiableCredentials.add(createVerifiableCredential("2", List.of("prefix:OtherType")));
        verifiableCredentials.add(createVerifiableCredential("3", List.of("gx:ServiceOffering")));

        // Action
        final List<VerifiableCredential> serviceOfferings = claimsCredentialsService.findServiceOfferings(verifiableCredentials);

        // Test
        assertThat(serviceOfferings).hasSize(2);
        assertThat(serviceOfferings)
                .extracting(vc -> VpVcUtil.getCredentialSubjects(vc.toMap()))
                .flatExtracting(mapSet -> mapSet.stream().map(map -> map.get("id")).toList())
                .containsExactlyInAnyOrder("1", "3");
    }

    @Test
    @DisplayName("IF ServiceOffering is existent THEN an empty list is returned")
    void testFindServiceOfferingsWithoutMatch() {
        // Prepare
        final Set<VerifiableCredential> verifiableCredentials = new HashSet<>();
        verifiableCredentials.add(createVerifiableCredential("1", List.of("gx:DataResource")));
        verifiableCredentials.add(createVerifiableCredential("2", List.of("gx:SoftwareResource")));
        verifiableCredentials.add(createVerifiableCredential("3", List.of("gx:LegalParticipant")));

        // Action
        final List<VerifiableCredential> serviceOfferings = claimsCredentialsService.findServiceOfferings(verifiableCredentials);

        // Test
        assertThat(serviceOfferings).isEmpty();
    }

    @Test
    @DisplayName("IF other than ServiceOfferings and Participant types are existent THEN those are returned")
    void testFindResourcesWithMatch() {
        // Prepare
        final Set<VerifiableCredential> verifiableCredentials = new HashSet<>();
        verifiableCredentials.add(createVerifiableCredential("1", List.of("gx:ServiceOffering")));
        verifiableCredentials.add(createVerifiableCredential("2", List.of("gx:LegalParticipant")));
        verifiableCredentials.add(createVerifiableCredential("3", List.of("gx:legalRegistrationNumber")));
        verifiableCredentials.add(createVerifiableCredential("4", List.of("gx:GaiaXTermsAndConditions")));
        verifiableCredentials.add(createVerifiableCredential("5", List.of("gx:DataResource")));
        verifiableCredentials.add(createVerifiableCredential("6", List.of("gx:PhysicalResource")));
        verifiableCredentials.add(createVerifiableCredential("7", List.of("prefix:CustomType")));

        // Action
        final List<VerifiableCredential> resourceOfferings = claimsCredentialsService.findResourceOfferings(verifiableCredentials);

        // Test
        assertThat(resourceOfferings).hasSize(3);
        assertThat(resourceOfferings)
                .extracting(vc -> VpVcUtil.getCredentialSubjects(vc.toMap()))
                .flatExtracting(mapSet -> mapSet.stream().map(map -> map.get("id")).toList())
                .containsExactlyInAnyOrder("5", "6", "7");
    }

    @Test
    @DisplayName("IF only ServiceOfferings and Participant types are existent THEN an empty list is returned")
    void testFindResourcesWithoutMatch() {
        // Prepare
        final Set<VerifiableCredential> verifiableCredentials = new HashSet<>();
        verifiableCredentials.add(createVerifiableCredential("1", List.of("gx:ServiceOffering")));
        verifiableCredentials.add(createVerifiableCredential("2", List.of("gx:LegalParticipant")));
        verifiableCredentials.add(createVerifiableCredential("3", List.of("gx:GaiaXTermsAndConditions")));

        // Action
        final List<VerifiableCredential> resourceOfferings = claimsCredentialsService.findResourceOfferings(verifiableCredentials);

        // Test
        assertThat(resourceOfferings).isEmpty();
    }

    private CredentialContainer createCredentialContainer(final List<String> domainSpecificIds, final List<String> gxIds) {
        final Set<VerifiableCredential> domainSpecificVCs = new HashSet<>();
        for (final String id : domainSpecificIds) {
            domainSpecificVCs.add(createVerifiableCredential(id, List.of("DomainType")));
        }

        final Set<VerifiableCredential> gxVCs = new HashSet<>();
        for (final String id : gxIds) {
            gxVCs.add(createVerifiableCredential(id, List.of("gx:ServiceOffering")));
        }

        return new CredentialContainer(domainSpecificVCs, gxVCs);
    }

    private VerifiableCredential createVerifiableCredential(final String id, final List<String> type) {
        final Map<String, Object> credentialSubject = new HashMap<>();
        credentialSubject.put("id", id);
        credentialSubject.put("type", type);
        final Map<String, Object> verifiableCredential = new HashMap<>();
        verifiableCredential.put("credentialSubject", credentialSubject);
        return VerifiableCredential.fromMap(verifiableCredential);
    }
}
