package com.msg.tests;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import com.msg.controller.ClaimComplianceProviderController;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class EndToEndTest {

    @Test
    @TestHTTPEndpoint(ClaimComplianceProviderController.class) 
    void testSendClaimsEndpoint() {
        // prepare
        String claims = """
        [{
        "id": "https://gaia-x.eu/.well-known/service1.json",
        "type": "gx:ServiceOffering",
        "gx:providedBy": {
            "id": "https://gaia-x.eu/.well-known/participant.json"
        },
        "gx:policy": "",
        "gx:termsAndConditions": {
            "gx:URL": "http://termsandconds.com",
            "gx:hash": "d8402a23de560f5ab34b22d1a142feb9e13b3143"
        },
        "gx:dataAccountExport": {
            "gx:requestType": "API",
            "gx:accessType": "digital",
            "gx:formatType": "application/json"
        }
        },
        {
        "id": "https://gaia-x.eu/.well-known/service2.json",
        "type": "gx:ServiceOffering",
        "gx:providedBy": {
            "id": "https://gaia-x.eu/.well-known/participant.json"
        },
        "gx:policy": "",
        "gx:termsAndConditions": {
            "gx:URL": "http://termsandconds.com",
            "gx:hash": "d8402a23de560f5ab34b22d1a142feb9e13b3143"
        },
        "gx:dataAccountExport": {
            "gx:requestType": "API",
            "gx:accessType": "digital",
            "gx:formatType": "application/json"
        }
        }]
        """;

        // action
        Response response = 
        given()
        .contentType(ContentType.JSON)
            .body(claims)
        .when()
            .post("/send-claims");

        // test
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().jsonPath().getList("type")).contains("VerifiablePresentation");
    }
}