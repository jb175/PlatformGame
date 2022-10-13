package com.jbmo60927.gamestates;

import com.jbmo60927.App;
import com.jbmo60927.ui.Component;

import java.awt.event.MouseEvent;


public class State {

    protected App app;

    public State(App game) {
        this.app = game;
    }

    public boolean isIn(MouseEvent e, Component input) {
        return input.getBounds().contains(e.getX(), e.getY());
    }

    public App getGame(){
        return this.app;
    }
}
