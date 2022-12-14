package com.jbmo60927.packets.join_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedJoinPacket extends ReceivedPacket implements JoinPacket {

    App app;

    public ReceivedJoinPacket(byte[] parameters, App app) {
        super(PacketType.JOIN, new byte[] {}, String.format("%s join the game", new String(parameters, StandardCharsets.UTF_8)));
        this.app = app;
    }

    @Override
    public void execute() {
    }
}
