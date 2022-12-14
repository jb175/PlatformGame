package com.jbmo60927.packets.version_packet;

import java.util.Objects;
import java.util.logging.Level;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.thread.ServiceThread;

public class ReceivedVersionPacket extends ReceivedPacket implements VersionPacket {

    private App app;
    private ServiceThread serviceThread;

    public ReceivedVersionPacket(byte[] parameters, App app, ServiceThread serviceThread) {
        super(PacketType.VERSION, new byte[] {}, "");
        serviceThread.setClientVersion(new String(parameters));
        this.serviceThread = serviceThread;
        this.app = app;
    }

    @Override
    public void execute() {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, () -> String.format("client version: %s server version: %s", serviceThread.getClientVersion(), app.getVersion()));
        if (!Objects.equals(serviceThread.getClientVersion(), app.getVersion())) {
            LOGGER.log(Level.INFO, () -> String.format("Version with client#%d is NOT the same", serviceThread.getClientNumber()));
            // will crash the server (we might clause the connection)
            // throw new VersionError();
        } else if (LOGGER.isLoggable(Level.INFO)){
            LOGGER.log(Level.FINE, () -> String.format("Version with client#%d is the same", serviceThread.getClientNumber()));
        }
    }
}
