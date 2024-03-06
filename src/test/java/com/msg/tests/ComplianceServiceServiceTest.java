package com.msg.tests;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msg.services.SdCreatorService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

@QuarkusTest
class ComplianceServiceServiceTest {

    @Inject
    SdCreatorService sdCreator;
    String LEGALPARTICIPANTPATH = Paths.get("..", "resources", "legalParticipant.json").toString();
    String LEGALREGISTRATIONNUMBERPATH = Paths.get("..", "resources", "legalRegistrationNumber.json").toString();
    String TERMSANDCONDITIONSPATH = Paths.get("..", "resources", "termsAndConditions.json").toString();

    @Test
    void testEndpoint() throws IOException {
        // Configure test data
        ObjectMapper objectMapper = new ObjectMapper();
        File legalParticipantFile = new File(LEGALPARTICIPANTPATH);
        File legalRegistrationNumberFile = new File(LEGALREGISTRATIONNUMBERPATH);
        File termsAndConditionsFile = new File(TERMSANDCONDITIONSPATH);
        Map<String, Object> legalParticipantVerifiableCredential = objectMapper.readValue(legalParticipantFile, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> legalRegistrationNumberVerifiableCredential = objectMapper.readValue(legalRegistrationNumberFile, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> termsAndConditionsVerifiableCredential = objectMapper.readValue(termsAndConditionsFile, new TypeReference<Map<String, Object>>() {});

        Set<Map<String, Object>> credentials = new HashSet<>(){{
            add(legalParticipantVerifiableCredential);
            add(legalRegistrationNumberVerifiableCredential);
            add(termsAndConditionsVerifiableCredential);
        }};

        // Execute test
        Map<String, Object> verifiablePresentation = sdCreator.transformVCsToVP(credentials);
        given()
          .body(verifiablePresentation)
          .header("Content-Type", "application/json")
          .when()
            .post("/call-compliance")
          .then()
            .statusCode(201)
            .body("type", hasItem("VerifiableCredential"));
    }
}