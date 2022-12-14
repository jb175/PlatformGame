package com.jbmo60927.packets.version_packet;

import java.util.Objects;
import java.util.logging.Level;

import com.jbmo60927.App;
import com.jbmo60927.errors.VersionError;
import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedVersionPacket extends ReceivedPacket implements VersionPacket {

    public ReceivedVersionPacket(byte[] parameters) {
        super(PacketType.VERSION, parameters, "");
    }

    @Override
    public void execute() {
        if (!Objects.equals(App.VERSION, new String(parameters))) {
            throw new VersionError(String.format("Version is not the same as the server%nclient version: %s server version: %s", App.VERSION, new String(parameters)));
        } else if (LOGGER.isLoggable(Level.INFO)){
            LOGGER.log(Level.INFO, "Version is the same as the server");
        }
    }
}
