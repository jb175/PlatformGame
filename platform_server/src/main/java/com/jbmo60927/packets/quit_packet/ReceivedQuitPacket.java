package com.jbmo60927.packets.quit_packet;

import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.utilz.Constants.PacketType;

public class ReceivedQuitPacket extends ReceivedPacket implements QuitPacket {

    public ReceivedQuitPacket(byte[] parameters) {
        super(PacketType.QUIT, parameters);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
    }
}
