package com.jbmo60927.packets.version_packet;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.thread.ServiceThread;
import com.jbmo60927.utilz.Constants.PacketType;
import java.util.Objects;
import java.util.logging.Level;

public class ReceivedVersionPacket extends ReceivedPacket implements VersionPacket {

    private App app;
    private ServiceThread serviceThread;

    public ReceivedVersionPacket(byte[] parameters, App app, ServiceThread serviceThread) {
        super(PacketType.VERSION, parameters);
        serviceThread.setClientVersion(new String(parameters));
        this.app = app;
        this.serviceThread = serviceThread;
    }

    @Override
    public void execute() {
        if (!Objects.equals(serviceThread.getClientVersion(), app.getVersion())) {
            throw new VersionError();
        } else {
            LOGGER.log(Level.FINE, "Versions are similare");
        }
    }

    /**
     * Version error that occure when the client and the server does'nt use the same version
     */
    class VersionError extends Error {
        public VersionError() {
            super("client version is not the one requested to connect to the server");
        }
    }
}
