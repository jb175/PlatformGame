package com.jbmo60927.utilz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.jbmo60927.utilz.HelpsMethods.bytesToInt;
import static com.jbmo60927.utilz.HelpsMethods.intToBytes;

import org.junit.jupiter.api.Test;

public class HelpsMethodsTest {
    @Test
    void testBytesToInt() {
        final int value = 1;
        
        //1 byte => converted
        byte[] byteArray = new byte[] {(byte) value};
        assertEquals(value, bytesToInt(byteArray));

        //up to 4 bytes => converted
        byteArray = new byte[] {(byte) value, (byte) value, (byte) value, (byte) value};
        assertEquals(value + value*256 + value*65536 + value*16777216, bytesToInt(byteArray));

        //but 0 byte => 0
        byteArray = new byte[] {};
        assertEquals(0, bytesToInt(byteArray));

        //and more than 4 bytes => 0
        byteArray = new byte[] {(byte) value, (byte) value, (byte) value, (byte) value, (byte) value};
        assertEquals(0, bytesToInt(byteArray));
    }

    @Test
    void testCanMoveHere() {

    }

    @Test
    void testGetEntityXPosNextToWall() {

    }

    @Test
    void testGetEntityYPosUnderAboveFloor() {

    }

    @Test
    void testIntToBytes() {
        //while value < 256
        final int value = 32;
        
        //return empty array while we wants 0 bytes
        assertEquals(true, testForEachValue(new byte[0], intToBytes(value, 0)));

        //else return an array with the x bytes containing the value
        assertEquals(true, testForEachValue(new byte[] {(byte) value}, intToBytes(value, 1)));
        assertEquals(true, testForEachValue(new byte[] {(byte) 0, (byte) value}, intToBytes(value, 2)));
        assertEquals(true, testForEachValue(new byte[] {(byte) 0, (byte) 0, (byte) value}, intToBytes(value, 3)));
        assertEquals(true, testForEachValue(new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) value}, intToBytes(value, 4)));
        
        //if value is too hight to be in the given bytes, it return an empty array
        assertEquals(true, testForEachValue(new byte[1], intToBytes(300, 1)));

        //return empty array while we wants more than 4 bytes
        assertEquals(true, testForEachValue(new byte[5], intToBytes(value, 5)));
    }

    @Test
    void testIsEntityOnFloor() {

    }

    /**
     * made a verification on array.
     * if array don't get the same length return false
     * then it compare all values to it's equivalent in other array, if their is a difference return false
     * otherwise return true
     * 
     * @param expected expected array
     * @param value given array
     * @return a boolean value to indicate if arrays are equivalents
     */
    public boolean testForEachValue(byte[] expected, byte[] value) {
        if (expected.length != value.length)
            return false;

        for (int i = 0; i < value.length; i++)
            if (value[i] != expected[i])
                return false;
        
        return true;
    }
}
