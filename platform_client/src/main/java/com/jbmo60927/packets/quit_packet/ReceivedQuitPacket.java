package com.jbmo60927.packets.quit_packet;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedQuitPacket extends ReceivedPacket implements QuitPacket {

    public ReceivedQuitPacket() {
        super(PacketType.QUIT, new byte[] {});
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
    }
}
