package com.jbmo60927.packets.set_level_packet;

import com.jbmo60927.levels.Level;
import com.jbmo60927.packets.SendPacket;
import static com.jbmo60927.utilz.HelpsMethods.intToBytes;


public class SendSetLevelPacket extends SendPacket {

    public SendSetLevelPacket(Level level) {
        super(PacketType.SETLEVEL, convertLevelDataToBytes(level.getLvlData(), 0));
    }

    /**
     * convert a level data into an array of bytes
     * @param leveldata
     * @return
     */
    private static byte[] convertLevelDataToBytes(int[][] leveldata, int levelNumber) {
        int levelHeight = leveldata.length;
        int levelLength = leveldata[0].length;
        byte[] byteLevelData = new byte[levelHeight*levelLength];

        for (int line=0; line<levelHeight; line++) {
            for (int column=0; column<levelLength; column++) {
                byteLevelData[line*levelLength+column] = (byte) leveldata[line][column];
            }
        }

        return compactPacket(new byte[][] {intToBytes(levelHeight, SetLevelPacket.LEVELHEIGHTBYTES), intToBytes(levelLength, SetLevelPacket.LEVELLENGHTBYTES), intToBytes(levelNumber, SetLevelPacket.LEVELNUMBERBYTES), byteLevelData});
    }
}
