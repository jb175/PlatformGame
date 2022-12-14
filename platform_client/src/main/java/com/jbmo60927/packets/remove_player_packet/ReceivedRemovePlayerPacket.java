package com.jbmo60927.packets.remove_player_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedRemovePlayerPacket extends ReceivedPacket implements RemovePlayerPacket {

    App app;

    public ReceivedRemovePlayerPacket(byte[] parameters, App app) {
        super(PacketType.REMOVEPLAYER, new byte[0], String.format("%s left the game", new String(parameters, StandardCharsets.UTF_8)));
        this.app = app;
    }

    @Override
    public void execute() {
    }
}
