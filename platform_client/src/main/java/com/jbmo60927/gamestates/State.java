package com.jbmo60927.gamestates;

import com.jbmo60927.main.Game;
import com.jbmo60927.ui.Component;

import java.awt.event.MouseEvent;


public class State {

    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, Component input) {
        return input.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame(){
        return game;
    }
}
