package com.template.qa;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.template.qa"})
@ConfigurationPropertiesScan("com.template.qa.props")
@AutoConfigureWireMock
public class TestConfig {
}
