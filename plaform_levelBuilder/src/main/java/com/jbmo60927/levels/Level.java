package com.jbmo60927.levels;

import com.jbmo60927.App;
import com.jbmo60927.ui.Component;
import com.jbmo60927.ui.LevelBuilderImage;
import com.jbmo60927.utilz.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Level {
    
    private LevelBuilderImage[][] lvlData;
    BufferedImage[] images;

    public Level(int columns, int rows) {
        this.images = Component.loadImages(LoadSave.LEVEL_ATLAS, 12, 4, App.TILE_DEFAULT_SIZE, App.TILE_DEFAULT_SIZE);
        this.lvlData = Level.setLvlData(columns, rows, this.images);
    }

    public LevelBuilderImage GetSpriteIndex(int x, int y) {
        return this.lvlData[y][x];
    }

    public LevelBuilderImage[][] getLvlData() {
        return this.lvlData;
    }

    public void setLvlData(int columns, int rows) {
        Level.setLvlData(columns, rows, this.images);
    }

    public void draw(Graphics g) {
        for (LevelBuilderImage[] dataColumns : lvlData) {
            for (LevelBuilderImage levelBuilderImage : dataColumns) {
                levelBuilderImage.draw(g);
            }
        }
    }

    public void update() {
        //nothing to update
    }

    private static LevelBuilderImage[][] setLvlData(int columns, int rows, BufferedImage[] images) {
        LevelBuilderImage[][] lvlData = new LevelBuilderImage[columns][rows];

        for (int i = 0; i < App.TILE_IN_WIDTH; i++) {
            for (int j = 0; j < App.TILE_IN_HEIGHT; j++) {
                lvlData[i][j] = new LevelBuilderImage(i*App.TILE_SIZE, j*App.TILE_SIZE, App.TILE_SIZE, App.TILE_SIZE, images);
            }
        }
        return lvlData;
    }
}
