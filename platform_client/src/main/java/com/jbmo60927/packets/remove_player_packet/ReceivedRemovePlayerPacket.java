package com.jbmo60927.packets.remove_player_packet;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedRemovePlayerPacket extends ReceivedPacket implements RemovePlayerPacket {

    public ReceivedRemovePlayerPacket() {
        super(PacketType.REMOVEPLAYER, new byte[0]);
    }

    @Override
    public void execute() {
    }
}
