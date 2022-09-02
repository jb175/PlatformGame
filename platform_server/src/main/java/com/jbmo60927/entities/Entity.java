package com.jbmo60927.entities;

public abstract class Entity {
    protected float x,y;
    protected Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public float[] getPosition() {
        return new float[] {x, y};
    }
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}