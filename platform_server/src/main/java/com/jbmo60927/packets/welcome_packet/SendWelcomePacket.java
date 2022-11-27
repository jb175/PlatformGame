package com.jbmo60927.packets.welcome_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.packets.SendPacket;

public class SendWelcomePacket extends SendPacket implements WelcomePacket{

    private static final String MESSAGE = "Welcome to the Platform Game. See more information at https://github.com/jb175/PlatformGame";

    public SendWelcomePacket() {
        super(PacketType.WELCOME, MESSAGE.getBytes(StandardCharsets.UTF_8));
    }
}
