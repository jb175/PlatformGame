package com.jbmo60927.packets;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Packet {

    protected final byte[] parameters;
    private final int packetType; //unused by received packets

    protected static final Logger LOGGER = Logger.getLogger(Packet.class.getName());

    protected Packet(int packetType, byte[] parameters) {
        LOGGER.setLevel(Level.INFO);
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
}
