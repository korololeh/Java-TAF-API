package com.template.qa.rest.api.service;

import com.template.qa.rest.ResponseWrapper;
import com.template.qa.rest.api.AbstractApi;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiService extends AbstractApi {

    private final RequestSpecification serviceClient;

    @Override
    protected RequestSpecification getClient() {
        return serviceClient;
    }

    public void sendRequest() {
        new ResponseWrapper<>(
                getClient().get(Endpoint.GET_REQUEST.getFilePath())) {
        };
    }

    @AllArgsConstructor
    @Getter
    public enum Endpoint {
        GET_REQUEST("/test");
        private final String filePath;
    }
}

