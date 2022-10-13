package com.jbmo60927.thread;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;

public class AcceptUserThread extends Thread{

    //number of clent connected since last restart
    private int clientNumber = 0;

    private ServerSocket listener;
    private App app;

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(AcceptUserThread.class.getName());

    public AcceptUserThread(ServerSocket listener, App app) {
        this.listener = listener;
        this.app = app;
    }

    @Override
    public void run() {
        try {
            while (!this.isInterrupted()) {
                // Accept client connection request
                // Get new Socket at Server.

                Socket socketOfServer = listener.accept();
                new ServiceThread(socketOfServer, clientNumber++, app).start();
            }
        } catch (Exception e) {
            if(!this.isInterrupted()) {
                LOGGER.log(Level.SEVERE, "Error while trying to accept new user", e);
            }
        }
    }
}
