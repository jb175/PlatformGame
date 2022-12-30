package com.jbmo60927.utilz;

public class HelpsMethods {
	private HelpsMethods() {
	  throw new IllegalStateException("Utility class");
	}

	/**
	 * return a byte array from the int
	 * @param i the value we wants to convert into bytes
	 * @param bytesSize the number of bytes we wants [1-4]
	 * @return the array
	 */
	public static byte[] intToBytes(int i, int bytesSize) {
		byte[] b = new byte[bytesSize];
		switch (bytesSize) {
			case 4:
				b[0] = (byte)((i >> 24) & 0xff);
				b[1] = (byte)((i >> 16) & 0xff);
				b[2] = (byte)((i >> 8) & 0xff);
				b[3] = (byte)((i >> 0) & 0xff);
				break;
			case 3:
				b[0] = (byte)((i >> 16) & 0xff);
				b[1] = (byte)((i >> 8) & 0xff);
				b[2] = (byte)((i >> 0) & 0xff);
				break;
			case 2:
				b[0] = (byte)((i >> 8) & 0xff);
				b[1] = (byte)((i >> 0) & 0xff);
				break;
			case 1:
				b[0] = (byte)(i & 0xff);
				break;
			default:
				break;
		}
		return b;
	}

    /**
	 * return an int from a byte array
	 * @param b the byte array we wants to convert (1-4 bytes)
	 * @return the corresponding int
	 */
	public static int bytesToInt(byte[] b) {
		int i = 0;
		switch (b.length) {
			case 4:
                i = ((b[0]) << 24) + ((b[1]) << 16) + ((b[2]) << 8) + (b[3] & 0xff);
				break;
			case 3:
                i = ((b[0]) << 16) + ((b[1]) << 8) + (b[2] & 0xff);
				break;
			case 2:
                i = ((b[0]) << 8) + (b[1] & 0xff);
				break;
			case 1:
                i = (b[0] & 0xff);
				break;
			default:
				break;
		}
		return i;
	}

	public static byte intToByte(int i) {
		return (byte)(i & 0xff);
	}

	public static int byteToInt(byte b) {
		return (b & 0xff);
	}

	public static byte charToByte(char c) {
		return (byte) c;
	}

	public static char byteToChar(byte b) {
		return (char) b;
	}

	public static byte[] stringToBytes(String s, java.nio.charset.Charset c) {
		return s.getBytes(c);
	}

	public static String bytesToString(byte[] b, java.nio.charset.Charset c) {
		return new String(b, c);
	}

	public static byte[] stringToBytes(String s) {
		return stringToBytes(s, java.nio.charset.StandardCharsets.UTF_8);
	}

	public static String bytesToString(byte[] b) {
		return bytesToString(b, java.nio.charset.StandardCharsets.UTF_8);
	}

	public static byte[] floatToBytes(float flt) {
		return intToBytes(Float.floatToIntBits(flt), 4);
	}

	public static float bytesToFloat(byte[] array) {
		float flt = 0;
		if (array.length == 4)
			flt = Float.intBitsToFloat(bytesToInt(array));
		return flt;
	}

	public static byte[] subByteArray(byte[] mainArray, int begining, int size) {
		if (mainArray.length >= begining+size) {
			byte[] subArray = new byte[size];
			for (int i = 0; i < subArray.length; i++)
				subArray[i] = mainArray[begining+i];
			return subArray;
		}
		return new byte[] {};
	}
}