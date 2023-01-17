package com.jbmo60927.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.gamestates.GameStates;
import com.jbmo60927.main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener, MouseWheelListener {

	private GamePanel gamePanel;

	private static final Logger LOGGER = Logger.getLogger(MouseInputs.class.getName());

	public MouseInputs(GamePanel gamePanel) {
		LOGGER.setLevel(Level.FINE);
		this.gamePanel = gamePanel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.log(Level.FINE, e.getLocationOnScreen().toString());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (LOGGER.isLoggable(Level.FINEST))
			LOGGER.log(Level.FINEST, e.getLocationOnScreen().toString());
		switch (GameStates.getGameState()) {
			case MENU:
				gamePanel.getApp().getMenu().mouseMoved(e);
				break;
			case DRAWING:
				gamePanel.getApp().getDrawing().mouseMoved(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.log(Level.FINE, e.getLocationOnScreen().toString());
		switch (GameStates.getGameState()) {
			case DRAWING:
				gamePanel.getApp().getDrawing().mouseClicked(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.log(Level.FINE, e.getLocationOnScreen().toString());
		switch (GameStates.getGameState()) {
			case MENU:
				gamePanel.getApp().getMenu().mousePressed(e);
				break;
			case DRAWING:
				gamePanel.getApp().getDrawing().mousePressed(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.log(Level.FINE, e.getLocationOnScreen().toString());
		switch (GameStates.getGameState()) {
			case MENU:
				gamePanel.getApp().getMenu().mouseReleased(e);
				break;
			case DRAWING:
				gamePanel.getApp().getDrawing().mouseReleased(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.log(Level.FINE, e.getLocationOnScreen().toString());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.log(Level.FINE, e.getLocationOnScreen().toString());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.log(Level.FINE, String.format("%d %d %d %d %d", e.getWheelRotation(), e.getScrollType(), e.getScrollAmount(), e.getX(), e.getY()));
		switch (GameStates.getGameState()) {
			case DRAWING:
				gamePanel.getApp().getDrawing().mouseWheelMoved(e);
				break;
			default:
				break;
		}
	}

}