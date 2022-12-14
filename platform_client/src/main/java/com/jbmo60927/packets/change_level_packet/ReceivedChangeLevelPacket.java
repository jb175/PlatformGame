package com.jbmo60927.packets.change_level_packet;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedChangeLevelPacket extends ReceivedPacket {

    App app;

    protected ReceivedChangeLevelPacket(byte[] parameters, App app) {
        super(PacketType.CHANGELEVEL, parameters, "");
        this.app = app;
    }

    @Override
    public void execute() {
    }
}
