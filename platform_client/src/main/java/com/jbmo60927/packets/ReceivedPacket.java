package com.jbmo60927.packets;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public abstract class ReceivedPacket extends Packet {

    protected ReceivedPacket(int packetType, byte[] parameters) {
        super(packetType, parameters);
        if (LOGGER.isLoggable(Level.FINEST))
            LOGGER.log(Level.FINEST, this.toString());
    }

    public String toString() {
        return new String(parameters, StandardCharsets.UTF_8);
    }

    public abstract void execute();
}
