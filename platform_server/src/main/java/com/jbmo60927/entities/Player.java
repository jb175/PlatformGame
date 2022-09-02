package com.jbmo60927.entities;

import static com.jbmo60927.utilz.Constants.PlayerConstants.*;

public class Player extends Entity {

	private int playerAction = IDLE;
    protected String name; //name of the player

    public Player(float x, float y, String name) {
        super(x, y);
        this.name = name;
    }

    public int getPlayerAction() {
        return playerAction;
    }

    public void setPlayerAction(int playerAction) {
        this.playerAction = playerAction;
    }

    public String getName() {
        return name;
    }
}
