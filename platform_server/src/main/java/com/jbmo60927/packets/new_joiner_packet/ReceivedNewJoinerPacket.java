package com.jbmo60927.packets.new_joiner_packet;

import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.utilz.Constants.PacketType;

public class ReceivedNewJoinerPacket extends ReceivedPacket implements NewJoinerPacket {

    public ReceivedNewJoinerPacket(byte[] parameters) {
        super(PacketType.NEWJOINER, parameters);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
    }
}