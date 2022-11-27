package com.jbmo60927.packets.welcome_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.packets.SendPacket;

public class SendWelcomePacket extends SendPacket implements WelcomePacket{

    private static final String MESSAGE = "Hi, i'm a new client";

    public SendWelcomePacket() {
        super(PacketType.WELCOME, MESSAGE.getBytes(StandardCharsets.UTF_8));
    }
}
