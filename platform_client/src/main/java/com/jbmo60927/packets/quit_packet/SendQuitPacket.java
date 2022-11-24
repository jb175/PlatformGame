package com.jbmo60927.packets.quit_packet;

import com.jbmo60927.packets.SendPacket;

public class SendQuitPacket extends SendPacket implements QuitPacket{

    public SendQuitPacket() {
        super(PacketType.QUIT, new byte[] {});
    }
}
