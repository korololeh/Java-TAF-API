package com.template.qa.cucumber.definitions;

import com.template.qa.rest.ResponseWrapper;
import com.template.qa.rest.api.service.ApiService;
import com.template.qa.rest.mocks.WiremockService;
import com.template.qa.utils.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;

@Slf4j
@RequiredArgsConstructor
public class ServiceDefinition {

    private final ApiService apiService;
    private final WiremockService wiremockService;
    private final ScenarioContext scenarioContext;

    @Given("Make request to server")
    public void makeRequestToServer() {
        apiService.sendRequest();
    }


    @Then("Response code is {string}")
    public void responseCodeIs(String responseCode) {
        ResponseWrapper.getLatestResponse().extract().response().getStatusCode();
        Assertions.assertThat(responseCode)
                .as("Response code isn't 200 --> ", responseCode)
                .isEqualTo("200");
    }
}
