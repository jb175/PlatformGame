package com.jbmo60927.packets.reception_packet;

import com.jbmo60927.packets.SendPacket;

public class SendReceptionPacket extends SendPacket implements ReceptionPacket{

    public SendReceptionPacket(Boolean valid) {
        super(PacketType.RECEPTION, new byte[] {(byte) (Boolean.TRUE.equals(valid)?1:0)});
    }
}
