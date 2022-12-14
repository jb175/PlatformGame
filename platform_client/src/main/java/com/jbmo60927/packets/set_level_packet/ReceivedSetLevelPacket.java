package com.jbmo60927.packets.set_level_packet;

import java.util.logging.Level;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;
import static com.jbmo60927.utilz.HelpsMethods.bytesToInt;

public class ReceivedSetLevelPacket extends ReceivedPacket {

    private App app;

    public ReceivedSetLevelPacket(byte[] parameters, App app) {
        super(PacketType.SETLEVEL, parameters, "");
        this.app = app;
    }

    @Override
    public void execute() {
        byte[][] parsedPacket = parsePacket(parameters, new int[] {SetLevelPacket.LEVELHEIGHTBYTES, SetLevelPacket.LEVELLENGHTBYTES, SetLevelPacket.LEVELNUMBERBYTES});
        int levelHeight = bytesToInt(parsedPacket[0]);
        int levelLength = bytesToInt(parsedPacket[1]);
        int levelNumber = bytesToInt(parsedPacket[2]);
        byte[] packetLevelData = parsedPacket[3];

        int[][] leveldata = new int[levelHeight][levelLength];
        if (packetLevelData.length == levelHeight*levelLength) {
            for (int i = 0; i < levelHeight; i++) {
                for (int j = 0; j < levelLength; j++) {
                    leveldata[i][j] = (packetLevelData[i*levelLength+j] & 0xff);
                }
            }
            app.getPlaying().getLevelHandler().addLevel(leveldata, levelNumber);
        } else {
            LOGGER.log(Level.SEVERE, "level packet has not a coherent number of data");
        }
        
        if (LOGGER.isLoggable(Level.FINEST))
            LOGGER.log(Level.FINEST, "");
    }
}
