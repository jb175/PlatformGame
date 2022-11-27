package com.jbmo60927.packets.join_packet;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedJoinPacket extends ReceivedPacket implements JoinPacket {

    public ReceivedJoinPacket() {
        super(PacketType.JOIN, new byte[] {});
    }

    @Override
    public void execute() {
    }
}
