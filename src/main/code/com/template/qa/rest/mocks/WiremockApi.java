package com.template.qa.rest.mocks;

import io.restassured.specification.RequestSpecification;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

/**
 * Helper class to configure wiremock server via HTTP calls
 */
@Slf4j
@SuppressWarnings("java:S1068")
@Component
@RequiredArgsConstructor
public class WiremockApi {

    private final RequestSpecification wiremockClient;

    public void importStubs(String payload) {
        wiremockClient.body(payload).post(Endpoint.BULK_MAPPING_IMPORT_URI);
    }

    public void resetRequests() {
        wiremockClient.delete(Endpoint.REQUESTS);
        log.trace("Reset all received requests for wiremock server");
    }

    public String getStub(String fileName) {
        return wiremockClient.get(fileName).asString();
    }

    @NoArgsConstructor(access = PRIVATE)
    static class Endpoint {

        static final String BULK_MAPPING_IMPORT_URI = "/__admin/mappings/import";
        static final String REQUESTS = "/__admin/requests";
    }
}
