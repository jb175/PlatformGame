package com.jbmo60927.entities;

/**
 * Every movable object in the game (all except the map).
 */
public abstract class Entity {

    protected float x,y; //position of the entity

    /**
     * Init the entity.
     * @param x //x coordinate
     * @param y //y coordinate
     */
    protected Entity(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Allow to read the X position of the entity.
     * @return the X position
     */
    public float getX() {
        return x;
    }

    /**
     * Allow to write the X position of the entity.
     * @param x new X position
     */
    public void setX(final float x) {
        this.x = x;
    }

    /**
     * Allow to read the Y position of the entity.
     * @return the Y position
     */
    public float getY() {
        return y;
    }

    /**
     * Allow to write the Y position of the entity.
     * @param y new Y position
     */
    public void setY(final float y) {
        this.y = y;
    }

    /**
     * Allow to read the position of the entity.
     * @return X and Y position of the entity
     */
    public float[] getPosition() {
        return new float[] {x, y};
    }

    /**
     * Allow to write the X and Y position of the entity.
     * @param x new X position
     * @param y new Y position
     */
    public void setPosition(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
}