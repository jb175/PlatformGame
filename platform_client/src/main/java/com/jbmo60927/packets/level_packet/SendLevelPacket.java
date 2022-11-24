package com.jbmo60927.packets.level_packet;

import com.jbmo60927.packets.SendPacket;

public class SendLevelPacket extends SendPacket implements LevelPacket{

    public SendLevelPacket(byte[] parameters) {
        super(PacketType.LEVEL, parameters);
    }
}
