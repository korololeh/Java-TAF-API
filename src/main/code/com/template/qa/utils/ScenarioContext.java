package com.template.qa.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static java.util.Optional.ofNullable;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class ScenarioContext {

    private final Map<Context, Object> context = new EnumMap<>(Context.class);

    public <T> void set(Context key, T value) {
        context.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Context key) {
        return (T) ofNullable(context.get(key)).orElse(null);
    }

    public enum Context {
        SCENARIO
    }
}
