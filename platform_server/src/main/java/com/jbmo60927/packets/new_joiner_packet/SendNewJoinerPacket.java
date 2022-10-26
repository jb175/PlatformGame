package com.jbmo60927.packets.new_joiner_packet;

import com.jbmo60927.packets.SendPacket;
import com.jbmo60927.utilz.Constants.PacketType;

public class SendNewJoinerPacket extends SendPacket implements NewJoinerPacket {

    public SendNewJoinerPacket(byte[] parameters) {
        super(PacketType.NEWJOINER, parameters);
    }
}
