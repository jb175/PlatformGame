package com.jbmo60927.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow {
	private final JFrame jframe;
	private final Game game;

	public GameWindow(final GamePanel gamePanel, final Game game) {
		this.game = game; //store game 
		jframe = new JFrame(); //create the frame

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //default action on closing button
		jframe.add(gamePanel); //link panel to the window
		//jframe.setLocationRelativeTo(null); //default position o nthe screen
		jframe.setTitle("PlatformGame"); //game title
		jframe.setResizable(false); //window can't be resized
		jframe.pack(); //set the size of the window to contain the panel default size
		jframe.setVisible(true); //display the window

		windowFocusChange();
		windowCloseAction();
	}

	/**
	 * action while gaining or loosing the window focus
	 */
	private void windowFocusChange() {
		jframe.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(final WindowEvent e) {
			}

			@Override
			/**
			 * stop the player when changing the focus window
			 */
			public void windowLostFocus(final WindowEvent e) {
				game.windowLostFocus();
			}
		});
	}

	/**
	 * stop the connection before closing the window
	 */
	private void windowCloseAction() {
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(final WindowEvent e) {
				if (game.getConnect().isConnected()) {
					game.getConnect().getGameLinkThread().close();
					
					//wait until disconnection
					long time = System.currentTimeMillis();
					while (game.getConnect().isConnected()) {
						if (System.currentTimeMillis() > time+10000) {
							throw new Error("Can't disconnect player");
						}
						//System.out.println(game.getConnect().isConnected());
					}
				}
			}
		});
	}
}