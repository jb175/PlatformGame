package com.jbmo60927.packets;

import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Packet {

	//header structure
	public static final int PACKETTYPEBYTES = 1;
	public static final int PACKETSIZEBYTES = 3;

    protected final int packetType; //unused by received packets
	protected final int packetSize;
    protected final byte[] parameters;

    protected static final Logger LOGGER = Logger.getLogger(Packet.class.getName());

    protected Packet(int packetType, int packetSize, byte[] parameters) {
        LOGGER.setLevel(Level.INFO);
		this.packetType = packetType;
		this.packetSize = packetSize;
        this.parameters = parameters;
    }

	/**
	 * Packet type class
	 * every packet first byte contain the type of the packet
	 * this byte is composed from 4 bits for the type and 4 others for the sub-type
	 */
	public static final class PacketType {
		private PacketType() {
			throw new IllegalStateException("Utility class");
		}

		//
		// Part 1: Connection with the server
		//

		//used to confirm the reception of a packet (only during connection initialization) (send by both)
		public static final byte RECEPTION 		= 0;
		//used to begin the communication between server and client (send by both)
		public static final byte WELCOME 		= 1;
		//used to compare version between client and server (send by both)
		public static final byte VERSION 		= 2;
		//used to initialize the player on the server (send by client)
		public static final byte JOIN 			= 3;
		//used to remove the player from the server (send by both)
		public static final byte QUIT 			= 4;

		//
		// Part 2: Connection with other client
		//

		//used to notify other clients that a new player arrive on the server (send by server)
		public static final byte NEWJOINER 		= 16;
		//used to update the position of an entity (send by both)
		public static final byte POSITION 		= 17;
		//used to notify other clients that a player has quit the server (send by server)
		public static final byte REMOVEPLAYER 	= 18;
		//used to init an entity (send by server)
		public static final byte NEWENTITY		= 19;

		//
		// Part 3: Level
		//

		//used to add a new level on the list (send by server)
		public static final byte SETLEVEL 		= 32;
		//used to add a remove level from the list (send by server)
		public static final byte REMOVELEVEL 	= 33;
	}

    public int getPacketType() {
        return packetType;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public byte[] getParameters() {
        return parameters;
    }
}
