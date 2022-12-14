package com.jbmo60927.packets;

import static com.jbmo60927.utilz.HelpsMethods.intToBytes;

import com.jbmo60927.errors.TooManyParametersError;

public abstract class SendPacket extends Packet {

    /**
     * to create a paquet and send him
     * @param packetType the type of the packet
     * @param parameters the parameters we wants to send with the packet
     */
    protected SendPacket(int packetType, byte[] parameters) {
        super(packetType, setPacketSize(parameters), parameters);
    }

    /**
     * this method is used to verify that the size of the packet is not bigger that it can be
     * (too many parameters compared to the size of the packet)
     * Otherwise the packet will not be understand by the receiver
     * @param parameters parameters to send
     * @return the length of the packet or raise an Error
     */
	private static int setPacketSize(byte[] parameters) {
		if (parameters != null && parameters.length < (1 << 8*Packet.PACKETSIZEBYTES))
			return parameters.length;
		else
            throw new TooManyParametersError("too many parameters in the packet");
	}

    /**
     * return the packet ready to be send
     * 
     * packet structure
     * Packet Type => PACKETTYPEBYTES byte
     * Packet Size => PACKETSIZEBYTES bytes
     * Parameters => X bytes depending of what we put inside the Packet Size
     * @return
     */
    public byte[] getPacket() {
        byte[] packetType = intToBytes(this.packetType, PACKETTYPEBYTES);
		byte[] packetSize = intToBytes(this.packetSize, PACKETSIZEBYTES);

        //creat the array
        byte[] packet = new byte[1+packetSize.length+parameters.length];
        for (int i = 0; i < packetType.length; i++) {
            packet[i] = packetType[i];
        }
        for (int i = 0; i < packetSize.length; i++) {
            packet[packetType.length+i] = packetSize[i];
        }
		if (this.packetSize > 0)
			for (int i = 0; i < this.packetSize; i++) {
				packet[packetType.length+packetSize.length+i] = parameters[i];
			}

        return packet;
    }

    /**
     * when we wants to send differents parameters it is easier to use this function
     * @param packetElements an array of bytes array (one for each sub parameters)
     * @return the array of bytes compacted
     */
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
