package com.msg.tests;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;

import com.msg.services.SdCreatorService;


@QuarkusTest
public class SdCreatorServiceTest {

    @Inject
    SdCreatorService sdCreator;

    @Test 
    public void testSendClaimsEndpoint() {
        HashMap<String, Object> claims1 = new HashMap<>() {{
            put("id", "https://gaia-x.eu/.well-known/service1.json");
            put("type", "gx:ServiceOffering");
            put("gx:providedBy", new HashMap<String, Object>() {{
                put("id", "https://gaia-x.eu/.well-known/participant.json");
            }});
            put("gx:policy", "");
            put("gx:termsAndConditions", new HashMap<String, Object>() {{
                put("gx:URL", "http://termsandconds.com");
                put("gx:hash", "d8402a23de560f5ab34b22d1a142feb9e13b3143");
            }});
            put("gx:dataAccountExport", new HashMap<String, Object>() {{
                put("gx:requestType", "API");
                put("gx:accessType", "digital");
                put("gx:formatType", "application/json");
            }});
        }};
        HashMap<String, Object> claims2 = new HashMap<>() {{
            put("id", "https://gaia-x.eu/.well-known/service2.json");
            put("type", "gx:ServiceOffering");
            put("gx:providedBy", new HashMap<String, Object>() {{
                put("id", "https://gaia-x.eu/.well-known/participant.json");
            }});
            put("gx:policy", "");
            put("gx:termsAndConditions", new HashMap<String, Object>() {{
                put("gx:URL", "http://termsandconds.com");
                put("gx:hash", "d8402a23de560f5ab34b22d1a142feb9e13b3143");
            }});
            put("gx:dataAccountExport", new HashMap<String, Object>() {{
                put("gx:requestType", "API");
                put("gx:accessType", "digital");
                put("gx:formatType", "application/json");
            }});
        }};
        Set<HashMap<String, Object>> claims = new HashSet<>(){{
            add(claims1);
            add(claims2);
        }};
        Set<HashMap<String, Object>> verifiableCredentials = sdCreator.transformClaimsToVCs(claims);
        System.out.println(verifiableCredentials);
        assertThat(verifiableCredentials, hasSize(2));
    }

    // @Test 
    // public void testSendVCsEndpoint() {
    //     Set<HashMap<String, Object>> claims = new HashSet<>();
    //     Map<String, Object> partOfClaims = new HashMap<>();
    //     claims.add(partOfClaims);
    // }
}