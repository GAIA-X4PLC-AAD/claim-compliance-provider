package com.msg.tests;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.msg.services.SdCreatorService;

@QuarkusTest
class SdCreatorServiceTest {

    @Inject
    SdCreatorService sdCreator;

    @Test
    void testSendClaimsEndpoint() {
        // prepare
        Map<String, Object> claims1 = new HashMap<>() {
            {
                put("id", "https://gaia-x.eu/.well-known/service1.json");
                put("type", "gx:ServiceOffering");
                put("gx:providedBy", new HashMap<String, Object>() {
                    {
                        put("id", "https://gaia-x.eu/.well-known/participant.json");
                    }
                });
                put("gx:policy", "");
                put("gx:termsAndConditions", new HashMap<String, Object>() {
                    {
                        put("gx:URL", "http://termsandconds.com");
                        put("gx:hash", "d8402a23de560f5ab34b22d1a142feb9e13b3143");
                    }
                });
                put("gx:dataAccountExport", new HashMap<String, Object>() {
                    {
                        put("gx:requestType", "API");
                        put("gx:accessType", "digital");
                        put("gx:formatType", "application/json");
                    }
                });
            }
        };
        Map<String, Object> claims2 = new HashMap<>() {
            {
                put("id", "https://gaia-x.eu/.well-known/service2.json");
                put("type", "gx:ServiceOffering");
                put("gx:providedBy", new HashMap<String, Object>() {
                    {
                        put("id", "https://gaia-x.eu/.well-known/participant.json");
                    }
                });
                put("gx:policy", "");
                put("gx:termsAndConditions", new HashMap<String, Object>() {
                    {
                        put("gx:URL", "http://termsandconds.com");
                        put("gx:hash", "d8402a23de560f5ab34b22d1a142feb9e13b3143");
                    }
                });
                put("gx:dataAccountExport", new HashMap<String, Object>() {
                    {
                        put("gx:requestType", "API");
                        put("gx:accessType", "digital");
                        put("gx:formatType", "application/json");
                    }
                });
            }
        };
        Set<Map<String, Object>> claims = new HashSet<>() {
            {
                add(claims1);
                add(claims2);
            }
        };

        // action
        Set<VerifiableCredential> verifiableCredentials = sdCreator.transformClaimsToVCs(claims);

        // test
        assertThat(verifiableCredentials).hasSize(2);
        for (VerifiableCredential credential : verifiableCredentials) {
            assertThat(verifyType(credential, "VerifiableCredential")).isTrue();
        }
    }

    boolean verifyType(VerifiableCredential credential, String expectedType) {
        List<String> typeValue = credential.getTypes();
        return typeValue.contains(expectedType);
    }
}