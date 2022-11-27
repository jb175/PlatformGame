package com.jbmo60927.levels;

import com.jbmo60927.App;
import com.jbmo60927.utilz.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class LevelHandler {

    public static final int LEVELNUMBER = 10;

    // private Game game;
    private BufferedImage[] levelSprite;
    private Level[] levels = new Level[LEVELNUMBER+1];
    private int currentLevel = 0;

    public LevelHandler(App game) {
        // this.game = game;
        importOutsideSprites();
        levels[0] = new Level(LoadSave.getLevelData());
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48]; //setup size on array to store all images from animations
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 12; i++) {
                int index = 12 * j + i;
                levelSprite[index] = img.getSubimage(i * App.TILE_DEFAULT_SIZE, j * App.TILE_DEFAULT_SIZE, App.TILE_DEFAULT_SIZE, App.TILE_DEFAULT_SIZE); //store the sub image on arrays
            }
    }

    public void draw(Graphics g) {
        
        for (int j = 0; j < App.TILE_IN_HEIGHT; j++)
            for (int i = 0; i < App.TILE_IN_WIDTH; i++) {
                int index = levels[currentLevel].GetSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i * App.TILE_SIZE, j * App.TILE_SIZE, App.TILE_SIZE, App.TILE_SIZE , null);
            }
    }

    public void update() {

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
            levels[number+1] = new Level(leveldata);
        }
    }
}
