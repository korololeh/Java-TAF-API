package com.template.qa.utils;

import groovy.util.logging.Slf4j;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Slf4j
@SuppressWarnings("java:S1118")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static String processNullValue(String cell) {
        if (cell.equals("[null]")) {
            return null;
        }
        if (cell.equals("[blank]")) {
            return "";
        }
        return cell;
    }
}
