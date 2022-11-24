package com.jbmo60927.packets.level_packet;

import com.jbmo60927.App;
import com.jbmo60927.packets.SendPacket;

public class SendLevelPacket extends SendPacket implements LevelPacket{

    public SendLevelPacket(byte[] parameters) {
        super(PacketType.LEVEL, parameters);
    }

    public SendLevelPacket(App app) {
        super(PacketType.LEVEL, ConvertLevelDataToBytes(app.getLevelHandler().getLevel(0).getLvlData()));
        
    }

    private static byte[] ConvertLevelDataToBytes(int[][] leveldata) {
        int columnSize = leveldata.length;
        int lineSize = leveldata[0].length;

        byte[] parameters = new byte[columnSize*lineSize+2];

        parameters[0]= (byte) columnSize;
        parameters[1]= (byte) lineSize;

        for (int line=0; line<columnSize; line++) {
            for (int column=0; column<lineSize; column++) {
                parameters[line*lineSize+column+2] = (byte) leveldata[line][column];
            }
        }

        return parameters;
    }
}
