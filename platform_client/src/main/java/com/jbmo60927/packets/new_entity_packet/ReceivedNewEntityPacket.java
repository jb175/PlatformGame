package com.jbmo60927.packets.new_entity_packet;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedNewEntityPacket extends ReceivedPacket implements NewEntityPacket {

    public ReceivedNewEntityPacket() {
        super(PacketType.NEWENTITY, new byte[] {});
    }

    @Override
    public void execute() {
    }
    
}
