package com.jbmo60927.levels;

import com.jbmo60927.main.Game;
import com.jbmo60927.utilz.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class LevelHandler {

    // private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelHandler(Game game) {
        // this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.getLevelData());
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48]; //setup size on array to store all images from animations
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 12; i++) {
                int index = 12 * j + i;
                levelSprite[index] = img.getSubimage(i * Game.TILE_DEFAULT_SIZE, j * Game.TILE_DEFAULT_SIZE, Game.TILE_DEFAULT_SIZE, Game.TILE_DEFAULT_SIZE); //store the sub image on arrays
            }
    }

    public void draw(Graphics g) {
        
        for (int j = 0; j < Game.TILE_IN_HEIGHT; j++)
            for (int i = 0; i < Game.TILE_IN_WIDTH; i++) {
                int index = levelOne.GetSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i * Game.TILE_SIZE, j * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE , null);
            }
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levelOne;
    }
}
