package com.template.qa.cucumber.hooks;

import io.cucumber.java.Before;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

import static com.template.qa.utils.LogUtils.PATH_TO_LOGS;

@Slf4j
public class CucumberHooks {

    @SneakyThrows
    @Before("@readLogs")
    public void clearLogFile() {
        log.info("Cleaning content of log file before the tests which are reading the log file");
        new PrintWriter(PATH_TO_LOGS).close();
    }
}
