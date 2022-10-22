package com.jbmo60927.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.jbmo60927.gamestates.GameStates;
import com.jbmo60927.main.GamePanel;

public class KeyboardInputs implements KeyListener {

	private final GamePanel gamePanel;

	public KeyboardInputs(final GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		switch (GameStates.getGameState()) {
			case MENU:
				gamePanel.getGame().getMenu().keyReleased(e);
				break;
			case CONNECT:
				gamePanel.getGame().getConnect().keyReleased(e);
				break;
			case PLAYING:
				gamePanel.getGame().getPlaying().keyReleased(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		switch (GameStates.getGameState()) {
			case MENU:
				gamePanel.getGame().getMenu().keyPressed(e);
				break;
			case CONNECT:
				gamePanel.getGame().getConnect().keyPressed(e);
				break;
			case PLAYING:
				gamePanel.getGame().getPlaying().keyPressed(e);
				break;
			default:
				break;
		}
	}
}