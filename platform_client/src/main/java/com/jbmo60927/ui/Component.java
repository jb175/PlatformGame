package com.jbmo60927.ui;

import com.jbmo60927.main.Game;
import com.jbmo60927.utilz.Constants;
import com.jbmo60927.utilz.LoadSave;

import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.FontMetrics;

public abstract class Component {

    protected int xPos, yPos, index=0, size;
    protected String text;
    protected BufferedImage[][] images;
    protected boolean mouseOver, mousePressed;
    protected Rectangle bounds;
    protected int totalWidth;
    protected int xOffsetCenter;
    protected FontMetrics metrics;

    public Component(int xPos, int yPos, int size, String text) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;
        this.text = text;
        this.totalWidth = Constants.UI.Component.WIDTH_BEGIN+Constants.UI.Component.WIDTH_MIDDLE*size+Constants.UI.Component.WIDTH_END;
        this.xOffsetCenter = totalWidth/2;
        loadImages();
        initBounds();
    }

    private void loadImages() {
        images = new BufferedImage[size+2][3];

        BufferedImage begin = LoadSave.getSpriteAtlas(LoadSave.COMPONENT_BEGIN);
        for (int j = 0; j < images[0].length; j++) {
            images[0][j] = begin.getSubimage(0, j*Constants.UI.Component.HEIGHT_DEFAULT, Constants.UI.Component.WIDTH_DEFAULT_BEGIN, Constants.UI.Component.HEIGHT_DEFAULT);
        }
        
        BufferedImage middle_0 = LoadSave.getSpriteAtlas(LoadSave.COMPONENT_MIDDLE_0);
        BufferedImage middle_1 = LoadSave.getSpriteAtlas(LoadSave.COMPONENT_MIDDLE_1);
        Boolean image1 = true;
        for (int i = 0; i < this.size; i++) {
            if (Math.random() < 0.25) {
                image1 = !image1;
            }
            if (image1) {
                for (int j = 0; j < images[0].length; j++) {
                    images[i+1][j] = middle_1.getSubimage(0, j*Constants.UI.Component.HEIGHT_DEFAULT, Constants.UI.Component.WIDTH_DEFAULT_MIDDLE, Constants.UI.Component.HEIGHT_DEFAULT);
                }
            } else {
                for (int j = 0; j < images[0].length; j++) {
                    images[i+1][j] = middle_0.getSubimage(0, j*Constants.UI.Component.HEIGHT_DEFAULT, Constants.UI.Component.WIDTH_DEFAULT_MIDDLE, Constants.UI.Component.HEIGHT_DEFAULT);
                }
            }
                
        }

        BufferedImage end = LoadSave.getSpriteAtlas(LoadSave.COMPONENT_END);
        for (int j = 0; j < images[0].length; j++) {
            images[size+1][j] = end.getSubimage(0, j*Constants.UI.Component.HEIGHT_DEFAULT, Constants.UI.Component.WIDTH_DEFAULT_END, Constants.UI.Component.HEIGHT_DEFAULT);
        }
    }

    private void initBounds() {
        bounds = new Rectangle(xPos-xOffsetCenter, yPos, totalWidth, Constants.UI.Component.HEIGHT);
    }

    public void draw(Graphics g) {
        int drawOffset = 0;
        g.drawImage(images[0][index], xPos - xOffsetCenter + drawOffset, yPos, Constants.UI.Component.WIDTH_BEGIN, Constants.UI.Component.HEIGHT, null);
        drawOffset+=Constants.UI.Component.WIDTH_BEGIN;
        for (int i = 0; i < size; i++) {
            g.drawImage(images[i+1][index], xPos - xOffsetCenter + drawOffset, yPos, Constants.UI.Component.WIDTH_MIDDLE, Constants.UI.Component.HEIGHT, null);
            drawOffset+=Constants.UI.Component.WIDTH_MIDDLE;
        }
        g.drawImage(images[size+1][index], xPos - xOffsetCenter + drawOffset, yPos, Constants.UI.Component.WIDTH_END, Constants.UI.Component.HEIGHT, null);
        //show box
        // g.drawRect(xPos-xOffsetCenter, yPos, totalWidth, Constants.UI.Component.HEIGHT);


        //font
        Font font = new Font("TimesRoman", Font.PLAIN, Constants.UI.Inputs.I_FONT_SIZE);
        g.setFont(font);
        metrics = g.getFontMetrics(font);

        //text offset
        int offset = 0;
        if(index==2)
            offset=(int)(4*Game.SCALE);
        g.drawString(text, xPos-metrics.stringWidth(text)/2, yPos+Constants.UI.Inputs.I_HEIGHT/2+2+offset);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    public void selectedUpdate(int writeAnimationStatus) {
        index = 1;
    }

    public void selectedUpdate() {
        index = 1;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public Rectangle getBounds() {
        return bounds;
    }
    
    public void mouseReleased(MouseEvent e) {

    }

    public void keyPressed(KeyEvent e) {
    }
}
