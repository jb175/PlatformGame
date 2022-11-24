package com.jbmo60927.packets.level_packet;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedLevelPacket extends ReceivedPacket implements LevelPacket{

    public ReceivedLevelPacket(byte[] parameters) {
        super(PacketType.LEVEL, parameters);
    }

    @Override
    public void execute() {
        if(LOGGER.isLoggable(Level.INFO))
            LOGGER.log(Level.INFO, new String(parameters, StandardCharsets.UTF_8));

        // int columnSize = parameters[0];
        // int lineSize = parameters[1];

        // int[][] level = new int[columnSize][lineSize];

        // for (int i = 0; i < columnSize; i++) {
        //     for (int j = 0; j < lineSize; j++) {
        //         level[i][j] = parameters[i*lineSize+j+2];
        //     }
        // }
    }
}
