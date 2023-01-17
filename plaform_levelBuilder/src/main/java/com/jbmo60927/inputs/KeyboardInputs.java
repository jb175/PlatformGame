package com.jbmo60927.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.gamestates.GameStates;
import com.jbmo60927.main.GamePanel;

public class KeyboardInputs implements KeyListener {

	private final GamePanel gamePanel;

	private static final Logger LOGGER = Logger.getLogger(KeyboardInputs.class.getName());

	public KeyboardInputs(final GamePanel gamePanel) {
		LOGGER.setLevel(Level.FINE);
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(final KeyEvent e) {
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.log(Level.FINE, Integer.toString(e.getKeyCode()));

	}

	@Override
	public void keyReleased(final KeyEvent e) {
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.log(Level.FINE, Integer.toString(e.getKeyCode()));
		switch (GameStates.getGameState()) {
			case MENU:
				gamePanel.getApp().getMenu().keyReleased(e);
				break;
			case DRAWING:
				gamePanel.getApp().getDrawing().keyReleased(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.log(Level.FINE, Integer.toString(e.getKeyCode()));
		switch (GameStates.getGameState()) {
			case MENU:
				gamePanel.getApp().getMenu().keyPressed(e);
				break;
			case DRAWING:
				gamePanel.getApp().getDrawing().keyPressed(e);
				break;
			default:
				break;
		}
	}
}