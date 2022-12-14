package com.jbmo60927.packets.new_joiner_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.entities.Player;
import com.jbmo60927.packets.SendPacket;

public class SendNewJoinerPacket extends SendPacket {

    public SendNewJoinerPacket(Player player) {
        super(PacketType.NEWJOINER, compactPacket(new byte[][] {player.getName().getBytes(StandardCharsets.UTF_8), }));
    }
}
