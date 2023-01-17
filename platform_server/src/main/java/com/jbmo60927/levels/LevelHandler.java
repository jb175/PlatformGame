package com.jbmo60927.levels;

import com.jbmo60927.utilz.LoadSave;

public class LevelHandler {

    public static final int LEVELNUMBER = 10;

    private Level[] levels = new Level[LEVELNUMBER];
    private int currentLevel = 0;

    public LevelHandler() {
        addLevel(LoadSave.getLevelData(), 0);
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
}
