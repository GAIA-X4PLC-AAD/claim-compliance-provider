package com.msg.ccp.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WireMockTestResource implements QuarkusTestResourceLifecycleManager {
    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        log.info("WireMock server started at port: " + wireMockServer.port());

        final Map<String, String> config = new HashMap<>();
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientDefault\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClient3dMapping\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientAscs\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientBmw\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientContinental\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientDlr\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientGx4fmPlcaad\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientInfineon\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientIviFraunhofer\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientMsgSystemsAg\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientSetlabs\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientTracetronic\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientTriangraphics\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientTuBerlin\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.sdcreator.clients.SdCreatorClientTuMuenchen\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.compliance.ComplianceServiceClient\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.ccp.catalogue.FederatedCatalogueClient\".url", wireMockServer.baseUrl());
        return config;
    }

    @Override
    public void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            wireMockServer = null;
        }
    }

    @Override
    public void inject(final TestInjector testInjector) {
        testInjector.injectIntoFields(wireMockServer, new TestInjector.AnnotatedAndMatchesType(InjectWireMock.class, WireMockServer.class));
    }
}
