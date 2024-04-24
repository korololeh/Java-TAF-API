package com.template.qa.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service")
public record ServiceProperties(String serviceBaseUri) {
}
