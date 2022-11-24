package com.jbmo60927.levels;

import com.jbmo60927.utilz.LoadSave;

public class LevelHandler {

    private Level[] levels = new Level[1];

    public LevelHandler() {
        levels[0] = new Level(LoadSave.getLevelData());
    }

    public Level getLevel(int levelNumber) {
        return levels[levelNumber];
    }
}
