package com.template.qa.rest.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.template.qa.props.MockProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.template.qa.utils.FileUtil.readFromFileNamed;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@SuppressWarnings("java:S1068")
@Slf4j
@RequiredArgsConstructor
@Component
public class WiremockService {

    private static final String COMMON = "common";
    private final WireMockServer wireMockServer;
    private final WiremockApi wiremockApi;
    private final MockProperties mockProperties;

    public void configureStub(final String mockName, final String stubFile) {
        ofNullable(mockProperties.commonStub()).ifPresent(stub -> importStub(COMMON, stub));
        importStub(mockName, stubFile);
    }

    public void resetRequests() {
        wiremockApi.resetRequests();
    }

    public void resetWireMockToDefaults() {
        wireMockServer.resetAll();
        log.info("Wiremock server is reset to defaults");
    }

    public List<ServeEvent> getEvents() {
        return wireMockServer.getAllServeEvents();
    }

    @SneakyThrows
    private void importStub(final String mockName, final String stubJsonFileName) {
        final String directory = format("%s/%s", mockProperties.resourceDir(), mockName);
        final String stubPayload = readFromFileNamed(directory, stubJsonFileName);
        wiremockApi.importStubs(stubPayload);
        log.debug("{} mock got new stub: {}", mockName, stubJsonFileName);
    }
}
