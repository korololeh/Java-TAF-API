package com.template.qa.rest;

import io.restassured.response.Response;
import io.restassured.response.ResponseBodyData;
import io.restassured.response.ValidatableResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import static org.hamcrest.Matchers.*;

@Slf4j
public abstract class ResponseWrapper<T> {

    private final ValidatableResponse response;

    @Getter
    private static ValidatableResponse latestResponse;

    public static <T> T getLatestResponse(Class<T> bodyType) {
        return latestResponse.extract().body().as(bodyType);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Type> type;

    private static final String DEFAULT_STATUS_CODE_ERROR = "Wrong status code!";

    protected ResponseWrapper(Response response) {
        this.response = response.then();
        latestResponse = this.response;
        setTypeRef();
    }

    private void setTypeRef() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            this.type = Optional.empty();
        } else {
            this.type = Optional
                    .ofNullable(((ParameterizedType) superClass).getActualTypeArguments()[0]);
        }
    }

    /**
     * Use {@link ResponseBodyData#asString()} in case of string response.
     */
    public T asDto() {
        expectStatusOk();
        return response.extract().body().as(type
                .orElseThrow(
                        () -> new NullPointerException("Return type not defined for ResponseWrapper")));
    }

    public ResponseWrapper<T> expectStatusOk() {
        return expectStatusOk(DEFAULT_STATUS_CODE_ERROR);
    }

    public ResponseWrapper<T> expectStatusOk(String errorMessage) {
        validateResponseCode(errorMessage, lessThan(300));
        return this;
    }

    public ResponseWrapper<T> expectStatusError() {
        return expectStatusError(DEFAULT_STATUS_CODE_ERROR);
    }

    public ResponseWrapper<T> expectStatusError(String errorMessage) {
        validateResponseCode(errorMessage, greaterThanOrEqualTo(400));
        return this;
    }

    public ResponseWrapper<T> expectStatusBadRequest() {
        validateResponseCode(equalTo(400));
        return this;
    }

    public ResponseWrapper<T> expectStatusConflict() {
        validateResponseCode(equalTo(409));
        return this;
    }

    public ResponseWrapper<T> expectStatusForbidden() {
        return expectStatusForbidden(DEFAULT_STATUS_CODE_ERROR);
    }

    public ResponseWrapper<T> expectStatusForbidden(String errorMessage) {
        validateResponseCode(errorMessage, equalTo(403));
        return this;
    }

    public ResponseWrapper<T> expectStatusNotFound() {
        validateResponseCode(equalTo(404));
        return this;
    }

    public ResponseWrapper<T> expectStatusAnyOf(Matcher<Integer> status1,
                                                Matcher<? super Integer> status2) {
        validateResponseCode(Matchers.anyOf(status1, status2));
        return this;
    }

    private void validateResponseCode(Matcher<? super Integer> expectedStatusCode) {
        validateResponseCode(DEFAULT_STATUS_CODE_ERROR, expectedStatusCode);
    }

    private void validateResponseCode(String errorMessage,
                                      Matcher<? super Integer> expectedStatusCode) {
        failOnServerError(response);
        try {
            response.statusCode(expectedStatusCode);
        } catch (AssertionError error) {
            throw new HttpException(errorMessage, error);
        }
    }

    private static void failOnServerError(ValidatableResponse response) {
        if (response.extract().statusCode() >= 500) {
            throw new HttpException(response.extract().body().asString());
        }
    }

    public ValidatableResponse response() {
        return response;
    }
}
