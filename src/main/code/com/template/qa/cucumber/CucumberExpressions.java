package com.template.qa.cucumber;

import io.cucumber.java.ParameterType;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.template.qa.utils.FileUtil.getResource;
import static java.nio.charset.Charset.defaultCharset;

public class CucumberExpressions {

    @SneakyThrows
    @ParameterType("\"([^\"]*?)\"")
    public String encodedString(String param) {
        return URLEncoder.encode(param, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    @ParameterType("\"([^\"]*?)\"")
    public String expectedResponse(String resourcePath) {
        final File resourceFile = getResource(resourcePath);
        return FileUtils.readFileToString(resourceFile, defaultCharset());
    }

}
