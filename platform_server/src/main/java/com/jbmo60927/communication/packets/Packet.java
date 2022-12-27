package com.jbmo60927.communication.packets;

import java.util.logging.Level;
import java.util.logging.Logger;


import static com.jbmo60927.utilz.HelpsMethods.intToBytes;
import static com.jbmo60927.utilz.HelpsMethods.bytesToInt;

import com.jbmo60927.communication.TypeList;
import com.jbmo60927.communication.parameters.Parameter;
import com.jbmo60927.thread.SendPacketThread;


public abstract class Packet {

    protected static final TypeList TYPES = new TypeList(new String[] {
			"reception", "welcome", "version", "join", "quit",
			"newjoiner", "position", "removeplayer", "newentity",
			"setlevel", "removelevel", "changelevel"}
		);

	//header structure
	public static final int PACKET_TYPE_BYTES = 1;
	public static final int PACKET_PARAMETER_NUMBER_BYTES = 1;

	//the type of the packet
    protected final int packetType;
	//the parameters of the packet
    protected final Parameter[] parameters;

	//the logger
    protected static final Logger LOGGER = Logger.getLogger(Packet.class.getName());

	/**
	 * the common part of all packets
	 * @param packetType the type of the packet
	 * @param packetSize the size of the packet
	 * @param parameters the parameters we have on this packet
	 */
    protected Packet(int packetType, Parameter[] parameters) {
        LOGGER.setLevel(Level.INFO);
		this.packetType = packetType;
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

		//null (not a real packet)
		public static final byte NULL 			= 0;
		//used to confirm the reception of a packet (only during connection initialization) (send by both)
		public static final byte RECEPTION 		= 1;
		//used to begin the communication between server and client (send by both)
		public static final byte WELCOME 		= 2;
		//used to compare version between client and server (send by both)
		public static final byte VERSION 		= 3;
		//used to initialize the player on the server (send by client)
		public static final byte JOIN 			= 4;
		//used to remove the player from the server (send by both)
		public static final byte QUIT 			= 5;

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
		//used to change the active level of a player (send by both)
		public static final byte CHANGELEVEL	= 34;
	}

	/**
	 * to access the value of the packet outside of himself
	 * @return the packet type
	 */
    public int getPacketType() {
        return packetType;
    }

	/**
	 * to access the value of the packet outside of himself
	 * @return the packet parameters
	 */
    public Parameter[] getParameters() {
        return parameters;
    }

	public static byte[] getCompactPacket(Packet packet) {
        byte[] packetType = intToBytes(packet.packetType, PACKET_TYPE_BYTES);
		byte[] parameterSize = intToBytes(packet.parameters.length, Parameter.PARAMETER_SIZE_BYTES);

		int totalSize = PACKET_TYPE_BYTES+Parameter.PARAMETER_SIZE_BYTES;
		for (Parameter parameter : packet.getParameters()) {
			totalSize += (Packet.PACKET_TYPE_BYTES+parameter.getValue().length);
		}

        //creat the array
        byte[] compactPacket = new byte[totalSize];
        for (int i = 0; i < packetType.length; i++) {
            compactPacket[i] = packetType[i];
        }
        for (int i = 0; i < parameterSize.length; i++) {
            compactPacket[PACKET_TYPE_BYTES+i] = parameterSize[i];
        }
		if (packet.parameters.length > 0) {
			int alreadyWrittenBytes = PACKET_TYPE_BYTES+Parameter.PARAMETER_SIZE_BYTES;
			for (int i = 0; i < packet.parameters.length; i++) {
				for (int j = 0; j < packet.parameters[i].getValue().length; j++) {
					compactPacket[alreadyWrittenBytes+i] = packet.parameters[i].getValue()[j];
				}
				alreadyWrittenBytes += packet.parameters[i].getValue().length;
			}
		}

        return compactPacket;
	}

    public static void receivePacket(byte[] packetType, Parameter[] parameters) {
		receivePacket(bytesToInt(packetType), parameters);
    }

    public static void sendPacket(int packetType, Parameter[] parameters, SendPacketThread sendThread) {
        Packet packet = createPacket(packetType, false, parameters);
		sendThread.sendPacket(packet);
    }

    private static void receivePacket(int packetType, Parameter[] parameters) {
        createPacket(packetType, true, parameters);
    }

    public static Packet createPacket(int packetType, boolean isReceived, Parameter[] parameters) {
        if(isReceived) {
            switch (packetType) {
                case PacketType.WELCOME:
					return new WelcomePacket(parameters);
				case PacketType.RECEPTION:
					return new WelcomePacket(parameters);
                default:
					return null;
            }
        } else {
			switch (packetType) {
                case PacketType.WELCOME:
					return new WelcomePacket();
				case PacketType.RECEPTION:
					return new WelcomePacket();
                default:
					return null;
            }
		}
    }

	public abstract String getMessage();
    public abstract void execute();
}