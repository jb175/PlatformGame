package com.jbmo60927.packets;

import java.util.Arrays;

public abstract class ReceivedPacket extends Packet {

    protected String message = "";

    protected ReceivedPacket(int packetType, byte[] parameters) {
        super(packetType, parameters.length, parameters);
    }

    public String getMessage() {
        return message;
    }

    //when the data is received what we need to do with it
    public abstract void execute();

	public static byte[][] parsePacket(byte[] packet, int[] parameterSize) {
		byte[][] parsedPacket = new byte[parameterSize.length+1][];
		int read = 0;
		for (int i = 0; i < parameterSize.length; i++) {
			parsedPacket[i] = Arrays.copyOfRange(packet, read, read+parameterSize[i]);
			read += parameterSize[i];
		}
		if (read != packet.length)
			parsedPacket[parameterSize.length] = Arrays.copyOfRange(packet, read, packet.length);

		return parsedPacket;
	}

	public static byte[] compactPacket(byte[][] packetElements) {
        int size = 0;
        for (byte[] pe : packetElements) {
            size += pe.length;
        }
		byte[] compactPacket = new byte[size];
        int write = 0;
        for (int i = 0; i < packetElements.length; i++) {
            for (int j = 0; j < packetElements[i].length; j++) {
                compactPacket[write] = packetElements[i][j];
                write++;
            }
        }

		return compactPacket;
	}
}
