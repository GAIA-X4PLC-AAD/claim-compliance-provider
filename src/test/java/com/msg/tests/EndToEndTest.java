package com.msg.tests;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import com.msg.controller.ClaimComplianceProviderController;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;


@QuarkusTest
class EndToEndTest {

    @Test
    @TestHTTPEndpoint(ClaimComplianceProviderController.class) 
    void testSendClaimsEndpoint() {
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
        given()
          .body(claims)
          .header("Content-Type", "application/json")
          .when()
            .post("/send-claims")
          .then()
            .statusCode(200)
            .body("type", hasItem("VerifiablePresentation"));
    }
}