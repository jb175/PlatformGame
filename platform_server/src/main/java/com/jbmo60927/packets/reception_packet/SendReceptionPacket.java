package com.jbmo60927.packets.reception_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.packets.SendPacket;

public class SendReceptionPacket extends SendPacket implements ReceptionPacket{

    private static final String MESSAGE = "";
    private static final byte[] packet;

    static {
        byte[] packetMessage = MESSAGE.getBytes(StandardCharsets.UTF_8);
        packet = new byte[packetMessage.length+1];
        packet[0] = PacketType.WELCOME;
        for (int i = 0; i < packetMessage.length; i++) {
            packet[i+1] = packetMessage[i];
        }
    }

    public SendReceptionPacket() {
        super(PacketType.WELCOME, packet);
    }
}
