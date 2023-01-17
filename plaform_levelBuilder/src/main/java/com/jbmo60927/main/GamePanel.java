package com.jbmo60927.main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.jbmo60927.inputs.KeyboardInputs;
import com.jbmo60927.inputs.MouseInputs;
import com.jbmo60927.App;

public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private KeyboardInputs keyboardInputs;
	private final App app;

	/**
	 * creat a space where we could draw images and used our keyboard and mouse
	 * @param app the parent
	 */
	public GamePanel(final App app) {
		this.app = app;
		setPanelSize();
		setInputs();
		setFocusTraversalKeysEnabled(false);
	}

	/**
	 * set the size of the panel (the window will be based on this size)
	 */
	private void setPanelSize() {
		final Dimension size = new Dimension(App.GAME_WIDTH, App.GAME_HEIGHT);
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
		addMouseWheelListener(mouseInputs);
	}

	/**
	 * method to update all images on the game
	 */
	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		app.render(g);
	}

	/**
	 * unpermanent method to make a link with the game
	 * @return
	 */
	public App getApp() {
		return app;
	}
}
