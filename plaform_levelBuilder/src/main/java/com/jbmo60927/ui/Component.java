package com.jbmo60927.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jbmo60927.utilz.LoadSave;

public class Component {
    protected int xPos, yPos, width, height;
    protected int imageIndex = 0;
    protected final BufferedImage[] images;

    protected Component(int xPos, int yPos, int width, int height, BufferedImage[] images) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.images = images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        if (imageIndex >= 0 && imageIndex < images.length)
            this.imageIndex = imageIndex;
    }

    public void draw(Graphics g) {
        g.drawImage(this.images[this.imageIndex], this.xPos, this.yPos, this.width, this.height, null);
    }

    public static BufferedImage[] loadImages(String path, int columns, int rows, int columnSize, int rowsSize) {
        BufferedImage img = LoadSave.getSpriteAtlas(path);
        BufferedImage[] sprites = new BufferedImage[columns*rows]; //setup size on array to store all images from animations
        for (int i = 0; i < columns; i++)
            for (int j = 0; j < rows; j++) {
                sprites[i*rows+j] = img.getSubimage(i * columnSize, j * rowsSize, columnSize, rowsSize); //store the sub image on arrays
            }
        return sprites;
    }
}
