package com.jbmo60927.gamestates;

import com.jbmo60927.App;
import com.jbmo60927.ui.Component;

import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;


public class State {

    protected App app;

    //logger for this class
    protected static final Logger LOGGER = Logger.getLogger(State.class.getName());
    
    public State(App game) {
        //set logger level
        LOGGER.setLevel(Level.INFO);
        
        this.app = game;
    }

    public boolean isIn(MouseEvent e, Component input) {
        return input.getBounds().contains(e.getX(), e.getY());
    }

    public App getGame(){
        return this.app;
    }
}
