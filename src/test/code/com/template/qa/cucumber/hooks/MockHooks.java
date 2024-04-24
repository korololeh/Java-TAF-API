package com.template.qa.cucumber.hooks;

import com.template.qa.rest.mocks.WiremockService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.template.qa.cucumber.TagUtilsKt.parseMockTagConfigs;

@Slf4j
@RequiredArgsConstructor
public class MockHooks {

    private final WiremockService wiremockService;
    private Map<String, String> requiredMocksForTest = new HashMap<>();

    @Before(value = "not @e2e and not @e2eflow")
    @SuppressWarnings("java:S5612")
    public void mockResponses(Scenario scenario) {
        requiredMocksForTest = parseMockTagConfigs(scenario.getSourceTagNames());
        requiredMocksForTest.forEach(wiremockService::configureStub);
    }

    @After(value = "not @e2e and not @e2eflow")
    public void resetMocks() {
        requiredMocksForTest.clear();
        wiremockService.resetWireMockToDefaults();
    }
}
