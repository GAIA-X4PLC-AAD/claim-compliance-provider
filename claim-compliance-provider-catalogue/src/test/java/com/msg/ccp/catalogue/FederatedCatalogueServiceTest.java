package com.msg.ccp.catalogue;

import com.danubetech.verifiablecredentials.VerifiablePresentation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.msg.ccp.exception.RestClientException;
import com.msg.ccp.interfaces.catalogue.CatalogueResponse;
import com.msg.ccp.wiremock.InjectWireMock;
import com.msg.ccp.wiremock.WireMockTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import jakarta.inject.Inject;
import org.apache.groovy.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
@QuarkusTestResource(WireMockTestResource.class)
class FederatedCatalogueServiceTest {
    private static final String PRESENTATION_400 = "presentationReturning400";
    private static final VerifiablePresentation INVALID_PAYLOAD = VerifiablePresentation.fromMap(Maps.of(PRESENTATION_400, PRESENTATION_400));

    @InjectWireMock
    WireMockServer wireMockServer;

    @Inject
    FederatedCatalogueService federatedCatalogueService;

    @BeforeEach
    public void setup() {
        configureFor("localhost", wireMockServer.port());
    }

    @Test
    @DisplayName("IF VP is valid THEN request is successful.")
    void successfulVerification() throws IOException {
        // prepare
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonRequest = readPayload();
        final Map<String, Object> jsonRequestAsMap = objectMapper.readValue(jsonRequest, new TypeReference<>() {});
        final String jsonResponse = objectMapper.writeValueAsString(createFederatedCatalogueResponse());

        stubFor(post(urlEqualTo("/verification"))
                .withRequestBody(equalToJson(jsonRequest, true, true))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody(jsonResponse)));

        // action
        final CatalogueResponse response = federatedCatalogueService.invoke(VerifiablePresentation.fromMap(jsonRequestAsMap));

        // test
        assertThat(response).isNotNull();
        assertThat(response.lifecycleStatus()).isEqualTo("active");
    }

    private String readPayload() throws IOException {
        return Files.readString(Path.of("src/test/resources/validPayload.json"));
    }

    @Test
    @DisplayName("IF VP is invalid THEN an exception is thrown.")
    void failingVerification() throws JsonProcessingException {
        // prepare
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonRequest = objectMapper.writeValueAsString(INVALID_PAYLOAD);

        stubFor(post(urlEqualTo("/verification"))
                .withRequestBody(equalToJson(jsonRequest, true, false))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("content-type", "application/json")
                        .withBody("""
                                {
                                    "code": "verification_error",
                                    "message": "Schema error: @prefix General:             <http://wwww.whatever.org/General> .\\n@prefix HD-Map:              <https://github.com/GAIA-X4PLC-AAD/map-and-scenario-data/HD-Map_ontology/> .\\n@prefix Scenario:            <https://github.com/GAIA-X4PLC-AAD/map-and-scenario-data/Scenario_ontology> .\\n@prefix dash:                <http://datashapes.org/dash#> .\\n@prefix dcat:                <http://www.w3.org/ns/dcat#> .\\n@prefix dct:                 <http://purl.org/dc/terms/> .\\n@prefix did:                 <https://www.w3.org/TR/did-core/#> .\\n@prefix environment-model:   <https://github.com/GAIA-X4PLC-AAD/ontology-management-base/tree/main/environment-model/> .\\n@prefix example:             <http://example.org/example/> .\\n@prefix foaf:                <http://xmlns.com/foaf/0.1/> .\\n@prefix gax-core:            <https://w3id.org/gaia-x/core#> .\\n@prefix gax-node:            <http://w3id.org/gaia-x/node#> .\\n@prefix gax-participant:     <http://w3id.org/gaia-x/participant#> .\\n@prefix gax-resource:        <http://w3id.org/gaia-x/resource#> .\\n@prefix gax-service:         <http://w3id.org/gaia-x/service#> .\\n@prefix gax-trust-framework: <https://w3id.org/gaia-x/gax-trust-framework#> .\\n@prefix gax-validation:      <https://w3id.org/gaia-x/validation#> .\\n@prefix gaxtrustframework:   <http://w3id.org/gaia-x/gax-trust-framework#> .\\n@prefix graphql:             <http://datashapes.org/graphql#> .\\n@prefix gx:                  <https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#> .\\n@prefix openlabel:           <https://openlabel.asam.net/V1-0-0/ontologies/> .\\n@prefix owl:                 <http://www.w3.org/2002/07/owl#> .\\n@prefix plc:                 <https://example.org/V1-0-0/plc/> .\\n@prefix rdf:                 <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix rdfs:                <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix sh:                  <http://www.w3.org/ns/shacl#> .\\n@prefix skos:                <http://www.w3.org/2004/02/skos/core#> .\\n@prefix surveyonto:          <http://semanticweb.org/metadatasurveyontology/> .\\n@prefix swa:                 <http://topbraid.org/swa#> .\\n@prefix tosh:                <http://topbraid.org/tosh#> .\\n@prefix vcard:               <http://www.w3.org/2006/vcard/ns#> .\\n@prefix xsd:                 <http://www.w3.org/2001/XMLSchema#> .\\n\\n[ rdf:type     sh:ValidationReport;\\n  sh:conforms  false;\\n  sh:result    [ rdf:type                      sh:ValidationResult;\\n                 sh:focusNode                  <https://www.example.org/edfe>;\\n                 sh:resultMessage              \\"Value does not have shape gx:DataAccountExportShape\\";\\n                 sh:resultPath                 gx:dataAccountExport;\\n                 sh:resultSeverity             sh:Violation;\\n                 sh:sourceConstraintComponent  sh:NodeConstraintComponent;\\n                 sh:sourceShape                [] ;\\n                 sh:value                      <_:b0>\\n               ]\\n] .\\n"
                                }
                                """)));

        // action and test
        assertThatThrownBy(() -> federatedCatalogueService.invoke(INVALID_PAYLOAD)).isInstanceOf(RestClientException.class)
                .hasMessageStartingWith("Schema error:");
    }

    @Test
    @DisplayName("IF response has no body THEN an exception is thrown.")
    void missingResponseBody() throws IOException {
        // prepare
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonRequest = readPayload();
        final Map<String, Object> jsonRequestAsMap = objectMapper.readValue(jsonRequest, new TypeReference<>() {});

        stubFor(post(urlEqualTo("/verification"))
                .withRequestBody(equalToJson(jsonRequest, true, false))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("content-type", "application/json")
                        ));

        // action and test
        assertThatThrownBy(() -> federatedCatalogueService.invoke(VerifiablePresentation.fromMap(jsonRequestAsMap))).isInstanceOf(RestClientException.class)
                .hasMessage("An error occurred while calling the federated catalogue");
    }

    private CatalogueResponse createFederatedCatalogueResponse() throws JsonProcessingException {
        final String responseString = """
                {
                    "verificationTimestamp": "2024-03-07T18:23:13.658412618Z",
                    "lifecycleStatus": "active",
                    "issuer": "did:web:compliance.lab.gaia-x.eu",
                    "issuedDateTime": "2023-12-06T12:43:21Z",
                    "validatorDids": [
                        "did:web:compliance.lab.gaia-x.eu"
                    ]
                }
                """;
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseString, CatalogueResponse.class);
    }
}
