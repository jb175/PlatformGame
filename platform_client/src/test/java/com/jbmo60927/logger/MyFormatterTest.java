package com.jbmo60927.logger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class MyFormatterTest {
    @Test
    void testDateFormatter() {
        assertArrayEquals("2022-12-08 12:20:17.862".toCharArray(), MyFormatter.dateFormatter(1670498417862L).toCharArray());
    }
}
