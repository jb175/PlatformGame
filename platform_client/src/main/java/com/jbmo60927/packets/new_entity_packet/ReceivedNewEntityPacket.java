package com.jbmo60927.packets.new_entity_packet;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedNewEntityPacket extends ReceivedPacket implements NewEntityPacket {

    App app;

    public ReceivedNewEntityPacket(byte[] parameters, App app) {
        super(PacketType.NEWENTITY, parameters, "");
        this.app = app;
    }

    @Override
    public void execute() {
    }
    
}
