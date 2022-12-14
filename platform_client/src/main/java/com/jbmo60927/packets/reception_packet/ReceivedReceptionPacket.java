package com.jbmo60927.packets.reception_packet;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedReceptionPacket extends ReceivedPacket implements ReceptionPacket{
    public ReceivedReceptionPacket() {
        super(PacketType.RECEPTION, new byte[] {}, "");
    }

    @Override
    public void execute() {
      // this execution will never do anything. it is used to confirm reception to the other part
    }
}