package com.template.qa.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.nonNull;
import static org.apache.commons.io.FileUtils.readFileToString;

@Slf4j
public class FileUtil {

    private FileUtil() {
    }

    public static String generateStringFromXMLFile(String fileName) throws IOException {
        return readFileToString(getResource("stepEvent/" + fileName + ".xml"), defaultCharset());
    }

    @SneakyThrows
    public static File getResource(String resourcePath) {
        final URL resource = Thread.currentThread().getContextClassLoader()
                .getResource(resourcePath);
        checkArgument(nonNull(resource), format("file %s should exist!", resourcePath));
        return new File(resource.toURI());
    }

    @SneakyThrows
    public static String readFromFileNamed(final String directory, final String fileName) {
        final var file = new File(getResource(directory), fileName);
        checkArgument(file.exists(), format("File %s not found!", file));
        return readFileToString(file, Charset.defaultCharset());
    }
}
