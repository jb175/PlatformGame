package com.jbmo60927.packets.remove_level_packet;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;
public class ReceivedRemoveLevelPacket extends ReceivedPacket implements RemoveLevelPacket {

    App app;

    public ReceivedRemoveLevelPacket(byte[] parameters, App app) {
        super(PacketType.REMOVELEVEL, parameters, "");
        this.app = app;
    }

    @Override
    public void execute() {
    }
}
