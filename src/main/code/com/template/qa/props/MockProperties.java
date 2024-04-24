package com.template.qa.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mocks")
public record MockProperties(
        String url,
        Integer port,
        String resourceDir,
        String commonStub
) {

}
