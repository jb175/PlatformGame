package com.jbmo60927.logger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class MyLoggerTest {
    @Test
    void testFileDateFormatter() {
        assertArrayEquals("2022-12-08_12:20:17".toCharArray(), MyLogger.fileDateFormatter(1670498417862L).toCharArray());
    }
}
