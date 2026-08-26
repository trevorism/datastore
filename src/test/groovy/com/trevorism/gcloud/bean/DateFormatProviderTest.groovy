package com.trevorism.gcloud.bean

import org.junit.jupiter.api.Test

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DateFormatProviderTest {

    @Test
    void testEachThreadGetsItsOwnInstance() {
        DateFormatProvider provider = new DateFormatProvider()
        def executor = Executors.newFixedThreadPool(2)
        try {
            def first = executor.submit({ provider.dateFormat } as Callable).get()
            def second = executor.submit({ provider.dateFormat } as Callable).get()

            assert !first.is(second)
        } finally {
            executor.shutdown()
        }
    }

    @Test
    void testRepeatedCallsOnOneThreadReuseTheInstance() {
        DateFormatProvider provider = new DateFormatProvider()

        assert provider.dateFormat.is(provider.dateFormat)
    }

    @Test
    void testConcurrentFormattingAndParsingIsStable() {
        DateFormatProvider provider = new DateFormatProvider()
        String formatted = "2024-01-02T03:04:05Z"
        Date expected = provider.dateFormat.parse(formatted)

        def executor = Executors.newFixedThreadPool(8)
        def failures = Collections.synchronizedList([])
        try {
            (1..400).each {
                executor.submit({
                    try {
                        if (provider.dateFormat.parse(formatted) != expected)
                            failures << "parse mismatch"
                        if (provider.dateFormat.format(expected) != formatted)
                            failures << "format mismatch"
                    } catch (Exception e) {
                        failures << e.toString()
                    }
                })
            }
            executor.shutdown()
            assert executor.awaitTermination(30, TimeUnit.SECONDS)
        } finally {
            executor.shutdownNow()
        }

        assert failures.isEmpty()
    }
}
