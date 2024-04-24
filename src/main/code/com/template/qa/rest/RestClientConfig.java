package com.template.qa.rest;

import com.template.qa.props.MockProperties;
import com.template.qa.props.ServiceProperties;
import com.template.qa.rest.api.filter.RestAssuredConsoleFilter;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.lang.String.format;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    static {
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Bean
    public RequestSpecification serviceClient(@Autowired ServiceProperties serviceProperties) {
        return RestAssured
                .given()
                .baseUri(serviceProperties.serviceBaseUri())
                .filter(new RestAssuredConsoleFilter())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    @Bean
    public RequestSpecification wiremockClient(@Autowired MockProperties mockProperties) {
        return RestAssured
                .given()
                .baseUri(format("%s:%s", mockProperties.url(), mockProperties.port()))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }
}
