package com.jbmo60927.packets;

import java.nio.charset.StandardCharsets;

public abstract class ReceivedPacket extends Packet {

    protected ReceivedPacket(int packetType, byte[] parameters) {
        super(packetType, parameters);
    }

    public String toString() {
        return new String(parameters, StandardCharsets.UTF_8);
    }

    public abstract void execute();
}
