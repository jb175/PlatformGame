package com.jbmo60927.packets.remove_level_packet;

import com.jbmo60927.packets.SendPacket;

public class SendRemoveLevelPacket extends SendPacket implements RemoveLevelPacket {

    protected SendRemoveLevelPacket() {
        super(PacketType.REMOVELEVEL, new byte[] {});
    }
}
