package com.jbmo60927.packets.welcome_packet;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedWelcomePacket extends ReceivedPacket implements WelcomePacket{
    public ReceivedWelcomePacket(byte[] parameters) {
        super(PacketType.WELCOME, parameters);
    }

    @Override
    public void execute() {
        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.log(Level.INFO, new String(parameters, StandardCharsets.UTF_8));
    }
}
