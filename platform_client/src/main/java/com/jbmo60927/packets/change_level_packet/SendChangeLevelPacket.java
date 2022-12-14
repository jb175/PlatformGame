package com.jbmo60927.packets.change_level_packet;

import com.jbmo60927.App;
import com.jbmo60927.packets.SendPacket;

public class SendChangeLevelPacket extends SendPacket {

    App app;

    protected SendChangeLevelPacket(byte[] parameters, App app) {
        super(PacketType.CHANGELEVEL, parameters);
        this.app = app;
    }
    
}
