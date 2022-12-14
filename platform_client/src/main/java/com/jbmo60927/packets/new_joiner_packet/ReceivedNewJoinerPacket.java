package com.jbmo60927.packets.new_joiner_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedNewJoinerPacket extends ReceivedPacket implements NewJoinerPacket {

    App app;

    public ReceivedNewJoinerPacket(byte[] parameters, App app) {
        super(PacketType.NEWJOINER, parameters, String.format("%s join the game", new String(parameters, StandardCharsets.UTF_8)));
        this.app = app;
    }

    @Override
    public void execute() {
    }
}
