package com.jbmo60927.packets.new_player_packet;

import com.jbmo60927.packets.SendPacket;

public class SendNewPlayerPacket extends SendPacket implements NewPlayerPacket {

    public SendNewPlayerPacket(byte[] parameters) {
        super(PacketType.NEWPLAYER, parameters);
    }
}
