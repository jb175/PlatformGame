package com.jbmo60927.packets.new_player_packet;

import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.utilz.Constants.PacketType;

public class ReceivedNewPlayerPacket extends ReceivedPacket implements NewPlayerPacket{

    public ReceivedNewPlayerPacket(byte[] parameters) {
        super(PacketType.NEWPLAYER, parameters);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
    }
}
