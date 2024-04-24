package com.template.qa.utils.json;

public record JsonModifier(Operation operation, Field field) {

    public enum Operation {
        DELETE, UPDATE, INSERT
    }

    public record Field(
            String path,
            String value
    ) {

    }
}
