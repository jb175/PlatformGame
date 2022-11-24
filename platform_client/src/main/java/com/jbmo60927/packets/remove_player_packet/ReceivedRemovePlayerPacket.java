package com.jbmo60927.packets.remove_player_packet;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedRemovePlayerPacket extends ReceivedPacket implements RemovePlayerPacket {

    public ReceivedRemovePlayerPacket(byte[] parameters) {
        super(PacketType.REMOVEPLAYER, parameters);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
    }
}