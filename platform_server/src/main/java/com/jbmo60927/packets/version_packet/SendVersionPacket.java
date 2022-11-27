package com.jbmo60927.packets.version_packet;

import com.jbmo60927.App;
import com.jbmo60927.packets.SendPacket;
import com.jbmo60927.thread.ServiceThread;
import java.util.Objects;

public class SendVersionPacket extends SendPacket implements VersionPacket {

    public SendVersionPacket(App app, ServiceThread serviceThread) {
        super(PacketType.VERSION, setParameters(app, serviceThread));
    }

    protected static byte[] setParameters(App app, ServiceThread serviceThread) {
        return new byte[Objects.equals(serviceThread.getClientVersion(), app.getVersion()) ? 1 : 0];
    }
}
