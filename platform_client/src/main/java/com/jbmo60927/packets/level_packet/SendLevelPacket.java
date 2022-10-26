package com.jbmo60927.packets.level_packet;

import com.jbmo60927.packets.SendPacket;
import com.jbmo60927.utilz.Constants.PacketType;

public class SendLevelPacket extends SendPacket implements LevelPacket{

    public SendLevelPacket(byte[] parameters) {
        super(PacketType.LEVEL, parameters);
    }
}
