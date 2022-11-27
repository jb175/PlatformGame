package com.jbmo60927.packets.new_joiner_packet;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedNewJoinerPacket extends ReceivedPacket implements NewJoinerPacket {

    public ReceivedNewJoinerPacket() {
        super(PacketType.NEWJOINER, new byte[0]);
    }

    @Override
    public void execute() {
    }
}
