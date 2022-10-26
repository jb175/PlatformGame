package com.jbmo60927.packets.version_packet;

import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.utilz.Constants.PacketType;

import java.util.logging.Level;

public class ReceivedVersionPacket extends ReceivedPacket implements VersionPacket {

    public ReceivedVersionPacket(byte[] parameters) {
        super(PacketType.VERSION, parameters);
    }

    @Override
    public void execute() {
        if(parameters[0] == 0)
            LOGGER.log(Level.INFO, "server version is the same");
        else
            throw new VersionError();
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
