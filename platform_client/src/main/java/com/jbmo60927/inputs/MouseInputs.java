package com.jbmo60927.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jbmo60927.gamestates.GameStates;
import com.jbmo60927.main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

	private GamePanel gamePanel;

	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch (GameStates.getGameState()) {
			case MENU:
				gamePanel.getGame().getMenu().mouseMoved(e);
				break;
			case CONNECT:
				gamePanel.getGame().getConnect().mouseMoved(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (GameStates.getGameState()) {
			case PLAYING:
				gamePanel.getGame().getPlaying().mouseClicked(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (GameStates.getGameState()) {
			case MENU:
				gamePanel.getGame().getMenu().mousePressed(e);
				break;
			case CONNECT:
				gamePanel.getGame().getConnect().mousePressed(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (GameStates.getGameState()) {
			case MENU:
				gamePanel.getGame().getMenu().mouseReleased(e);
				break;
			case CONNECT:
				gamePanel.getGame().getConnect().mouseReleased(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}