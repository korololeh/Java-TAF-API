package com.template.qa.utils;

import org.awaitility.core.ConditionFactory;
import org.hamcrest.Matcher;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.awaitility.Awaitility.await;

public class WaitUtils {

    public static final int ONE_SECOND = 1;
    public static final int TWO_SECONDS = 2;
    public static final int FIVE_SECONDS = 5;
    public static final int THIRTY_SECONDS = 30;
    public static final int DEFAULT_TIMEOUT = 75;


    private WaitUtils() {
    }

    public static ConditionFactory kindlyWait(final long seconds) {
        return await().atMost(seconds, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(TWO_SECONDS))
                .pollInSameThread();
    }

    public static void waitForNotExist(final boolean condition) {
        await()
                .atMost(Duration.of(FIVE_SECONDS, SECONDS))
                .pollDelay(Duration.of(TWO_SECONDS, SECONDS))
                .until(() -> condition);
    }

    public static <T> T waitUntil(final long seconds, final Callable<T> call,
                                  final Predicate<T> predicate) {
        return await()
                .atMost(seconds, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(TWO_SECONDS))
                .pollInSameThread()
                .until(call, predicate);
    }

    public static <T> T waitUntil(final long seconds, final Callable<T> call,
                                  final Matcher<Object> matcher) {
        return await()
                .atMost(seconds, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(TWO_SECONDS))
                .pollInSameThread()
                .until(call, matcher);
    }

    public static <T> T waitUntil(final Callable<T> call, final Matcher<Object> matcher) {
        return waitUntil(DEFAULT_TIMEOUT, call, matcher);
    }

    public static <T> T waitUntil(final Callable<T> call, final Predicate<T> predicate) {
        return waitUntil(DEFAULT_TIMEOUT, call, predicate);
    }

    public static void justWait(final long seconds) {
        await().pollDelay(Duration.ofSeconds(seconds)).until(() -> true);
    }
}
