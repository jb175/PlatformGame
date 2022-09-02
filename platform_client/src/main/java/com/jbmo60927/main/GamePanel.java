package com.jbmo60927.main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.jbmo60927.inputs.KeyboardInputs;
import com.jbmo60927.inputs.MouseInputs;

public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private KeyboardInputs keyboardInputs;
	private final Game game;

	/**
	 * creat a space where we could draw images and used our keyboard and mouse
	 * @param game the parent
	 */
	public GamePanel(final Game game) {
		this.game = game;
		setPanelSize();
		setInputs();
		setFocusTraversalKeysEnabled(false);
	}

	/**
	 * set the size of the panel (the window will be based on this size)
	 */
	private void setPanelSize() {
		final Dimension size = new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT);
		setPreferredSize(size);
	}

	/**
	 * set the inputs
	 */
	private void setInputs() {
		mouseInputs = new MouseInputs(this);
		keyboardInputs = new KeyboardInputs(this);
		addKeyListener(keyboardInputs);
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	/**
	 * method to update all images on the game
	 */
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	/**
	 * unpermanent method to make a link with the game
	 * @return
	 */
	public Game getGame() {
		return game;
	}
}
