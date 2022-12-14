package com.jbmo60927.entities;

import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Graphics;
/**
 * entity: a class to control all movable object
 */
public abstract class Entity {
    protected float x,y; //position of the object on the screen
    protected int width, height;
    protected Rectangle2D.Float hitbox;

    //level of the player
    protected int level;

    /**
     * setup the default postion of an entity
     * @param x
     * @param y
     */
    protected Entity(final float x, final float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    /**
     * set both position x and y with one method
     * @param x
     * @param y
     */
    public void setPosition(final float x, final float y) {
        this.hitbox.x = x;
        this.hitbox.y = y;
    }

    protected void initHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    /**
     * debuging hitbox
     * @param g
     */
    protected void drawHitbox(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    // protected void updateHitbox() {
    //     hitbox.x = (int) x;
    //     hitbox.y = (int) y;
    // }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}