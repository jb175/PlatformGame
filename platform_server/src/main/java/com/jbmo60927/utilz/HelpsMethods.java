package com.jbmo60927.utilz;

public class HelpsMethods {
	private HelpsMethods() {
	  throw new IllegalStateException("Utility class");
	}

	/**
	 * return a byte array from the int
	 * @param value the value we wants to convert into bytes
	 * @param bytesSize the number of bytes we wants [1-4]
	 * @return the array
	 */
	public static byte[] intToBytes(int value, int bytesSize) {
		byte[] array = new byte[bytesSize];
		switch (bytesSize) {
			case 4:
				array[0] = (byte)((value >> 24) & 0xff);
				array[1] = (byte)((value >> 16) & 0xff);
				array[2] = (byte)((value >> 8) & 0xff);
				array[3] = (byte)((value >> 0) & 0xff);
				break;
			case 3:
				array[0] = (byte)((value >> 16) & 0xff);
				array[1] = (byte)((value >> 8) & 0xff);
				array[2] = (byte)((value >> 0) & 0xff);
				break;
			case 2:
				array[0] = (byte)((value >> 8) & 0xff);
				array[1] = (byte)((value >> 0) & 0xff);
				break;
			case 1:
				array[0] = (byte)(value & 0xff);
				break;
			default:
				break;
		}
		return array;
	}

    /**
	 * return an int from a byte array
	 * @param array the byte array we wants to convert (1-4 bytes)
	 * @return the corresponding int
	 */
	public static int bytesToInt(byte[] array) {
		int value = 0;
		switch (array.length) {
			case 4:
                value = ((array[0]) << 24) + ((array[1]) << 16) + ((array[2]) << 8) + (array[3] & 0xff);
				break;
			case 3:
                value = ((array[0]) << 16) + ((array[1]) << 8) + (array[2] & 0xff);
				break;
			case 2:
                value = ((array[0]) << 8) + (array[1] & 0xff);
				break;
			case 1:
                value = (array[0] & 0xff);
				break;
			default:
				break;
		}
		return value;
	}
}
