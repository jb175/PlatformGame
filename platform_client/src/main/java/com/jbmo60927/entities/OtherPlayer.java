package com.jbmo60927.entities;

/**
 * player: a class used by every player herited from entities that contain everything to control and display the player 
 */
public class OtherPlayer extends Player {


    /**
     * set default values for a player
     * 
     * @param x position x
     * @param y position y
     */
    public OtherPlayer(final float x, final float y, String name) {
        super(x, y, name); //send default position to entities
        this.name = name;
    }

    public int getPlayerAction() {
        return playerAction;
    }

    public void setPlayerAction(int playerAction) {
        this.playerAction = playerAction;
    }
}
