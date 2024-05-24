package com.msg.tests;

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

        Map<String, String> config = new HashMap<>();
        config.put("quarkus.rest-client.\"com.msg.services.SdCreatorClient\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.services.ComplianceServiceClient\".url", wireMockServer.baseUrl());
        config.put("quarkus.rest-client.\"com.msg.services.catalogue.FederatedCatalogueClient\".url", wireMockServer.baseUrl());
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
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(wireMockServer, new TestInjector.AnnotatedAndMatchesType(InjectWireMock.class, WireMockServer.class));
    }
}
