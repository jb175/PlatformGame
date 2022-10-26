package com.jbmo60927.packets.version_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.App;
import com.jbmo60927.packets.SendPacket;
import com.jbmo60927.utilz.Constants.PacketType;

public class SendVersionPacket extends SendPacket implements VersionPacket {

    private static final byte[] packet;

    static {
        packet= App.VERSION.getBytes(StandardCharsets.UTF_8);;
    }

    public SendVersionPacket() {
        super(PacketType.VERSION, packet);
    }
}
