package com.jbmo60927.utilz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import static com.jbmo60927.utilz.HelpsMethods.byteToChar;
import static com.jbmo60927.utilz.HelpsMethods.charToByte;
import static com.jbmo60927.utilz.HelpsMethods.bytesToInt;
import static com.jbmo60927.utilz.HelpsMethods.intToByte;
import static com.jbmo60927.utilz.HelpsMethods.bytesToString;
import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

public class HelpsMethodsTest {
    @Test
    void testByteToChar() {
        assertEquals('a', byteToChar((byte) 'a'));
    }

    @Test
    void testBytesToInt() {
        assertEquals(1, bytesToInt(new byte[] {(byte)(1 & 0xff)}));

    }

    @Test
    void testBytesToString() {

    }

    @Test
    void testBytesToString2() {

    }

    @Test
    void testCharToByte() {

    }

    @Test
    void testIntToByte() {

    }

    @Test
    void testIntToBytes() {

    }

    @Test
    void testStringToBytes() {

    }

    @Test
    void testStringToBytes2() {

    }
}
