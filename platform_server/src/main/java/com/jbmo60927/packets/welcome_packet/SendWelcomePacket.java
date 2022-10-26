package com.jbmo60927.packets.welcome_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.packets.SendPacket;
import com.jbmo60927.utilz.Constants.PacketType;

public class SendWelcomePacket extends SendPacket implements WelcomePacket{

    private static final String MESSAGE = "Welcome to the Platform Game. See more information at https://github.com/jb175/PlatformGame";
    private static final byte[] packet;

    static {
        byte[] packetMessage = MESSAGE.getBytes(StandardCharsets.UTF_8);
        packet = new byte[packetMessage.length+1];
        packet[0] = PacketType.WELCOME;
        for (int i = 0; i < packetMessage.length; i++) {
            packet[i+1] = packetMessage[i];
        }
    }

    public SendWelcomePacket() {
        super(PacketType.WELCOME, packet);
    }
}
