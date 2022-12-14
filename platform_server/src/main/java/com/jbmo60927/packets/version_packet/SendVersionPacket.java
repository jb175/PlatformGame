package com.jbmo60927.packets.version_packet;

import com.jbmo60927.App;
import com.jbmo60927.packets.SendPacket;

import java.nio.charset.StandardCharsets;

public class SendVersionPacket extends SendPacket implements VersionPacket {

    public SendVersionPacket(App app) {
        super(PacketType.VERSION, app.getVersion().getBytes(StandardCharsets.UTF_8));
    }
}
