package com.jbmo60927.packets.remove_level_packet;

import com.jbmo60927.packets.ReceivedPacket;
public class ReceivedRemoveLevelPacket extends ReceivedPacket implements RemoveLevelPacket {

    public ReceivedRemoveLevelPacket() {
        super(PacketType.REMOVELEVEL, new byte[] {});
    }

    @Override
    public void execute() {
    }
}
