package com.jbmo60927.packets.position_packet;

import com.jbmo60927.packets.SendPacket;

public class SendPositionPacket extends SendPacket implements PositionPacket {

    public SendPositionPacket() {
        super(PacketType.POSITION, new byte[0]);
    }
}
