package com.jbmo60927.packets.welcome_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.packets.SendPacket;

public class SendWelcomePacket extends SendPacket {

    private static final String MESSAGE = "Welcome to the Platform Game. See more information at https://github.com/jb175/PlatformGame";

    public SendWelcomePacket() {
        int[][] packetStructure = WelcomePacket.packetStructure;
        super(PacketType.WELCOME, MESSAGE.getBytes(StandardCharsets.UTF_8));
    }
}
