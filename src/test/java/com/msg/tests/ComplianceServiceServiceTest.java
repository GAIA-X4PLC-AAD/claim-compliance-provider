package com.msg.tests;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msg.services.SdCreatorService;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class ComplianceServiceServiceTest {

    @Inject
    SdCreatorService sdCreator;
    String LEGALPARTICIPANTPATH = Paths.get("src", "test", "java", "com", "msg", "resources", "legalParticipant.json").toString();
    String LEGALREGISTRATIONNUMBERPATH = Paths.get("src", "test", "java", "com", "msg", "resources", "legalRegistrationNumber.json").toString();
    String TERMSANDCONDITIONSPATH = Paths.get("src", "test", "java", "com", "msg", "resources", "termsAndConditions.json").toString();

    @Test
    void testEndpoint() throws IOException {
        // prepare
        ObjectMapper objectMapper = new ObjectMapper();
        File legalParticipantFile = new File(LEGALPARTICIPANTPATH);
        File legalRegistrationNumberFile = new File(LEGALREGISTRATIONNUMBERPATH);
        File termsAndConditionsFile = new File(TERMSANDCONDITIONSPATH);
        VerifiableCredential legalParticipantVerifiableCredential = objectMapper.readValue(legalParticipantFile, new TypeReference<VerifiableCredential>() {});
        VerifiableCredential legalRegistrationNumberVerifiableCredential = objectMapper.readValue(legalRegistrationNumberFile, new TypeReference<VerifiableCredential>() {});
        VerifiableCredential termsAndConditionsVerifiableCredential = objectMapper.readValue(termsAndConditionsFile, new TypeReference<VerifiableCredential>() {});

        Set<VerifiableCredential> credentials = new HashSet<>(){{
            add(legalParticipantVerifiableCredential);
            add(legalRegistrationNumberVerifiableCredential);
            add(termsAndConditionsVerifiableCredential);
        }};

        // action
        VerifiablePresentation verifiablePresentation = sdCreator.transformVCsToVP(credentials);
        Response response = 
        given()
          .contentType(ContentType.JSON)
          .body(verifiablePresentation)
        .when()
          .post("/call-compliance");

        // test
        assertThat(response.getStatusCode()).isEqualTo(201);
        assertThat(response.getBody().jsonPath().getList("type")).contains("VerifiableCredential");
    }
}