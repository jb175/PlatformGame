package com.jbmo60927.packets.reception_packet;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedReceptionPacket extends ReceivedPacket implements ReceptionPacket{
    public ReceivedReceptionPacket(byte[] parameters) {
        super(PacketType.WELCOME, parameters);
    }

    @Override
    public void execute() {
        if (LOGGER.isLoggable(Level.INFO) && parameters!=null)
            LOGGER.log(Level.INFO, new String(parameters, StandardCharsets.UTF_8));
    }
}
