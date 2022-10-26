package com.jbmo60927.packets.level_packet;

import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.utilz.Constants.PacketType;

public class ReceivedLevelPacket extends ReceivedPacket implements LevelPacket{

    public ReceivedLevelPacket(byte[] parameters) {
        super(PacketType.LEVEL, parameters);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
    }
}
