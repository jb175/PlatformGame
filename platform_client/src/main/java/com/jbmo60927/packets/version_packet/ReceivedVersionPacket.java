package com.jbmo60927.packets.version_packet;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedVersionPacket extends ReceivedPacket implements VersionPacket {

    public ReceivedVersionPacket(byte[] parameter) {
        super(PacketType.VERSION, parameter);
    }

    @Override
    public void execute() {
    }
}
