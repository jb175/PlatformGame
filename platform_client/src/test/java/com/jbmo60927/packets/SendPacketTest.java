package com.jbmo60927.packets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import com.jbmo60927.entities.MovablePlayer;
import com.jbmo60927.packets.join_packet.SendJoinPacket;

public class SendPacketTest {
    @Test
    void testCompactPacket() {
        MovablePlayer player = new MovablePlayer(0, 0);
        player.setName("test");
        SendPacket packet = new SendJoinPacket(player);
        // packetType (1) | packetParametersLength (3) | playerName (20) |
        assertArrayEquals(new byte[] {4, 0, 0, 20, 116, 101, 115, 116, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, packet.getPacket());
    }

    @Test
    void testGetPacket() {

    }
}
