package com.jbmo60927.packets.position_packet;

import com.jbmo60927.packets.SendPacket;
import com.jbmo60927.utilz.Constants.PacketType;

public class SendPositionPacket extends SendPacket implements PositionPacket {

    public SendPositionPacket(byte[] parameters) {
        super(PacketType.POSITION, parameters);
    }
}
