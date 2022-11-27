package com.jbmo60927.packets.new_entity_packet;

import com.jbmo60927.packets.SendPacket;

public class SendNewEntityPacket extends SendPacket implements NewEntityPacket {

    protected SendNewEntityPacket(byte[] parameters) {
        super(PacketType.NEWENTITY, parameters);
    }
    
}
