package com.jbmo60927.packets.join_packet;

import com.jbmo60927.packets.SendPacket;

public class SendJoinPacket extends SendPacket implements JoinPacket {

    public SendJoinPacket() {
        super(PacketType.JOIN, new byte[] {});
    }
}
