package com.jbmo60927.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.jbmo60927.App;

public class GameWindow {
	private final JFrame jframe;
	private final App app;

    //logger for this class
    protected static final Logger LOGGER = Logger.getLogger(GameWindow.class.getName());

	public GameWindow(final GamePanel gamePanel, final App app) {
        //set logger level
        LOGGER.setLevel(Level.INFO);
		
		this.app = app; //store game 
		jframe = new JFrame(); //create the frame

		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //default action on closing button
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
				//nothing happen when the client come back to the app
			}

			@Override
			/**
			 * stop the player when changing the focus window
			 */
			public void windowLostFocus(final WindowEvent e) {
				app.windowLostFocus();
			}
		});
	}

	/**
	 * stop the connection before closing the window
	 */
	private void windowCloseAction() {
		jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				try {
					if (app.getConnect().isConnected()) {
						//app.getConnect().getGameLinkThread().sendPacket(new SendQuitPacket());
						
						//wait until disconnection
						long time = System.currentTimeMillis();
						while (app.getConnect().isConnected()) {
							if (System.currentTimeMillis() > time+3000)
								LOGGER.log(Level.SEVERE, "cant disconnect player");
						}
					}
				} finally {
					app.saveData();
				}
			}
		});
	}
}