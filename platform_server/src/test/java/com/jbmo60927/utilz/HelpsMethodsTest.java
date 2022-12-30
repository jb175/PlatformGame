package com.jbmo60927.utilz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import static com.jbmo60927.utilz.HelpsMethods.byteToChar;
import static com.jbmo60927.utilz.HelpsMethods.bytesToInt;

public class HelpsMethodsTest {
    @Test
    void testByteToChar() {
        assertEquals('a', byteToChar((byte) 'a'));
    }

    @Test
    void testBytesToInt() {
        assertEquals(1, bytesToInt(new byte[] {(byte)(1 & 0xff)}));

    }
}
