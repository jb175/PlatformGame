package com.jbmo60927.packets;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public abstract class SendPacket extends Packet {

    protected SendPacket(int packetType, byte[] parameters) {
        super(packetType, parameters);
    }

    public String toString() {
        byte[] packet = new byte[parameters.length+2];
        
        //first and last
        packet[0] = (byte) packetType;
        packet[parameters.length+1] = (byte) packetType;

        for (int i = 0; i < parameters.length; i++) {
            packet[i+1] = parameters[i];
        }
        String pck = new String(packet, StandardCharsets.UTF_8);
        LOGGER.log(Level.FINEST, pck);
        return pck;
    }
}
