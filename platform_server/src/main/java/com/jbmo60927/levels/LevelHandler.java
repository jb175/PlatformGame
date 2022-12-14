package com.jbmo60927.levels;

import java.util.ArrayList;

import com.jbmo60927.packets.set_level_packet.SendSetLevelPacket;
import com.jbmo60927.utilz.LoadSave;

public class LevelHandler {

    public static final int LEVELNUMBER = 10;

    private Level[] levels = new Level[LEVELNUMBER];
    private int currentLevel = 0;

    public LevelHandler() {
        levels[0] = new Level(LoadSave.getLevelData());
    }

    public Level getCurrentLevel() {
        return levels[currentLevel];
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Level[] getLevels() {
        return levels;
    }

    /**
     * 
     * @param leveldata
     * @param number between 0 and 10
     */
    public void addLevel(int[][] leveldata, int number) {
        if (number >= 0 && number < LEVELNUMBER) {
            levels[number] = new Level(leveldata);
        }
    }

    public SendSetLevelPacket[] sendLevels() {
        ArrayList<SendSetLevelPacket> levelsPacket = new ArrayList<>();
        for (int i = 0; i < levels.length; i++)
            if (levels[i] != null)
                levelsPacket.add(new SendSetLevelPacket(levels[i], i));
        return levelsPacket.toArray(new SendSetLevelPacket[levelsPacket.size()]);
    }
}
