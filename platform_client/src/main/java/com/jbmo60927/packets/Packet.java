package com.jbmo60927.packets;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Packet {

    protected final byte[] parameters;
    protected final int packetType; //unused by received packets

    protected static final Logger LOGGER = Logger.getLogger(Packet.class.getName());

    protected Packet(int packetType, byte[] parameters) {
        LOGGER.setLevel(Level.FINEST);
        this.parameters = parameters;
        this.packetType = packetType;
    }

    public byte[] getData() {
        return parameters;
    }

    /**
     * How the data is send
     */
    public abstract String toString();

	/**
	 * Packet type class
	 * every packet first byte contain the type of the packet
	 * this byte is composed from 4 bits for the type and 4 others for the sub-type
	 */
	public static final class PacketType {
		private PacketType() {
			throw new IllegalStateException("Utility class");
		}
		//Player
		public static final int NEWJOINER 		= 0;
		public static final int NEWPLAYER 		= 1;
		public static final int POSITION 		= 2;
		public static final int REMOVEPLAYER 	= 3;
		//Connection
		public static final int JOIN 			= 16;
		public static final int QUIT 			= 17;
		public static final int WELCOME 		= 18;
		public static final int VERSION 		= 19;
		public static final int LEVEL 			= 20;
		public static final int RECEPTION		= 21;
	}
}
