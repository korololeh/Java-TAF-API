package com.template.qa.utils;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class LogUtils {

    public static final String PATH_TO_LOGS = "./build/log/service.log";

    private LogUtils() {
    }

    @SneakyThrows
    public static String getLogsAsString() {
        final File logFile = new File(PATH_TO_LOGS);
        return FileUtils.readFileToString(logFile, UTF_8);
    }
}
