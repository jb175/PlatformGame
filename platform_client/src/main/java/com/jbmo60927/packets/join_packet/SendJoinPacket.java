package com.jbmo60927.packets.join_packet;

import com.jbmo60927.entities.MovablePlayer;
import com.jbmo60927.packets.SendPacket;
import static com.jbmo60927.utilz.HelpsMethods.floatToBytes;
import static com.jbmo60927.utilz.HelpsMethods.intToBytes;;

public class SendJoinPacket extends SendPacket {

    public SendJoinPacket(MovablePlayer player) {
        super(PacketType.JOIN, convertPlayerDataToBytes(player));
    }

    private static byte[] convertPlayerDataToBytes(MovablePlayer player) {
        byte[] name = new byte[JoinPacket.PLAYERNAMEBYTES];
        if (player.getName().length() > 0 && player.getName().length() <= JoinPacket.PLAYERNAMEBYTES) {
            byte[] xpos = floatToBytes(player.getX());
            byte[] ypos = floatToBytes(player.getY());
            byte[] playerAction = intToBytes(player.getPlayerAction(), JoinPacket.PLAYERACTIONBYTES);
            for (int i = 0; i < player.getName().length(); i++) {
                name[i] = (byte) player.getName().charAt(i);
            }

            return compactPacket(new byte[][] {name, xpos, ypos, playerAction});
        }
        return new byte[0];
    }
}
