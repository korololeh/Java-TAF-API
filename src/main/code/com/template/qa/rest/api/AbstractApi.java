package com.template.qa.rest.api;

import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractApi {

    protected abstract RequestSpecification getClient();
}
