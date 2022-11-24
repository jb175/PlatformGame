package com.jbmo60927.packets;

import java.nio.charset.StandardCharsets;

public abstract class SendPacket extends Packet {

    protected SendPacket(int packetType, byte[] parameters) {
        super(packetType, parameters);
    }

    public String toString() {
        byte[] packet = new byte[parameters.length+1];
        packet[0] = (byte) packetType;
        for (int i = 0; i < parameters.length; i++) {
            packet[i+1] = parameters[i];
        }
        return new String(packet, StandardCharsets.UTF_8);
    }
}
