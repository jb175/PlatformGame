package com.jbmo60927.packets;

import java.util.Arrays;

public abstract class ReceivedPacket extends Packet {

    //the message to display on log if not received during initilization
    protected String message;

    /**
     * to create a paquet from what we received
     * @param packetType the packet type
     * @param parameters the parameters of the packet
     * @param message the message to display on log if not received during initilization
     */
    protected ReceivedPacket(int packetType, byte[] parameters, String message) {
        super(packetType, parameters.length, parameters);
        this.message = message;
    }

    /**
     * what we wants to log if received outside from the initialization
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * when the data is received what we need to do with it
     */
    public abstract void execute();

    /**
     * when we expect different parameters we can use tis function to make sub-parts from the parameter array
     * @param packet the array of parameter we receive
     * @param parameterSize the byte size we have for every parameter expected
     * @return return an array where the last element is the "rest" of the array
     */
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
}
