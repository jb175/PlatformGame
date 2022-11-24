package com.jbmo60927.packets.position_packet;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedPositionPacket extends ReceivedPacket implements PositionPacket {

    public ReceivedPositionPacket(byte[] parameters) {
        super(PacketType.POSITION, parameters);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
    }
}
