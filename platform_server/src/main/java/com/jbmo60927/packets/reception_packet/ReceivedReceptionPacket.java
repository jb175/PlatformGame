package com.jbmo60927.packets.reception_packet;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedReceptionPacket extends ReceivedPacket implements ReceptionPacket{
    public ReceivedReceptionPacket() {
        super(PacketType.RECEPTION, new byte[] {});
    }

    @Override
    public void execute() {
        if (LOGGER.isLoggable(Level.INFO) && parameters!=new byte[] {})
            LOGGER.log(Level.INFO, new String(parameters, StandardCharsets.UTF_8));
    }
}
