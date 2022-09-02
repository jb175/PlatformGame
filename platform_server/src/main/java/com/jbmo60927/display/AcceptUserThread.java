package com.jbmo60927.display;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class AcceptUserThread extends Thread{

    //number of clent connected since last restart
    private int clientNumber = 0;

    private ServerSocket listener;
    private Game game;

    public AcceptUserThread(ServerSocket listener, Game game) {
        this.listener = listener;
        this.game = game;

    }

    @Override
    public void run() {
        try {
            while (!this.isInterrupted()) {
                // Accept client connection request
                // Get new Socket at Server.

                Socket socketOfServer = listener.accept();
                new ServiceThread(socketOfServer, clientNumber++, game).start();
            }
        } catch (Exception e) {
            if(!this.isInterrupted()) {
                Game.getLogger().log(Level.SEVERE, "Error while trying to accept new user", e);
            }
        }
    }
}
