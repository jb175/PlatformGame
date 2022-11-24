package com.jbmo60927.packets.remove_player_packet;

import com.jbmo60927.packets.SendPacket;

public class SendRemovePlayerPacket extends SendPacket implements RemovePlayerPacket {

    public SendRemovePlayerPacket(byte[] parameters) {
        super(PacketType.REMOVEPLAYER, parameters);
    }
}
