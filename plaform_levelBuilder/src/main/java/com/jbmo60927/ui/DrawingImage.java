package com.jbmo60927.ui;

import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseWheelEvent;

import com.jbmo60927.App;
import com.jbmo60927.utilz.LoadSave;

import java.awt.Graphics;
import java.awt.Color;

public class DrawingImage {

    private int xPos = 27*App.TILE_SIZE;
    private int yPos = Math.round(6.5f*App.TILE_SIZE);
    private int index = 0;

    private UserInput[] components = new UserInput[0];

    private BufferedImage[] levelSprite;
    

    public DrawingImage() {
        importOutsideSprites();
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

        if (index > 1)
            g.drawImage(levelSprite[index-1], xPos, yPos - 2*App.TILE_SIZE, App.TILE_SIZE, App.TILE_SIZE, null);
        g.drawImage(levelSprite[index], xPos, yPos, App.TILE_SIZE, App.TILE_SIZE, null);
        if (index < levelSprite.length-2)
            g.drawImage(levelSprite[index+1], xPos, yPos + 2*App.TILE_SIZE, App.TILE_SIZE, App.TILE_SIZE, null);

        g.setColor(Color.RED);
        g.drawRect(xPos, yPos, App.TILE_SIZE, App.TILE_SIZE);
    }

    public void update() {

    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() > 0 && index < levelSprite.length-1) {
            index++;
        } else if (e.getWheelRotation() < 0 && index > 0) {
            index--;
        }
	}
}
