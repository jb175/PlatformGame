package com.jbmo60927.entities;

import static com.jbmo60927.utilz.Constants.PlayerConstants.*;

/**
 * Class to store data for every player.
 */
public class Player extends Entity {

	private int playerAction = IDLE; //default player action
    protected String name; //name of the player

    /**
     * player builder.
     * @param x default X position
     * @param y default Y position
     * @param name name of the player
     */
    public Player(final float x, final float y, final int level, final String name) {
        super(x, y, level);
        this.name = name;
    }

    /**
     * get the actual player action.
     * @return the action the player is actually doing
     */
    public int getPlayerAction() {
        return playerAction;
    }

    /**
     * set the player action.
     * @param playerAction the new action the player is doing
     */
    public void setPlayerAction(final int playerAction) {
        this.playerAction = playerAction;
    }

    /**
     * get the player name.
     * @return the name
     */
    public String getName() {
        return name;
    }
}
