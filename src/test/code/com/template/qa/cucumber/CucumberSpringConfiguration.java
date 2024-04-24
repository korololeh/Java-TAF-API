package com.template.qa.cucumber;

import com.template.qa.TestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {TestConfig.class})
@CucumberContextConfiguration
public class CucumberSpringConfiguration {

}
