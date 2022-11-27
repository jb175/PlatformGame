package com.jbmo60927.packets.version_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.App;
import com.jbmo60927.packets.SendPacket;

public class SendVersionPacket extends SendPacket implements VersionPacket {

    public SendVersionPacket() {
        super(PacketType.VERSION, App.VERSION.getBytes(StandardCharsets.UTF_8));
    }
}
