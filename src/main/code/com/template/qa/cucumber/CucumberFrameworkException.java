package com.template.qa.cucumber;

/**
 * Throw this when Cucumber customization should fail
 */
public class CucumberFrameworkException extends RuntimeException {

    public CucumberFrameworkException(String message) {
        super(message);
    }

    public CucumberFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
