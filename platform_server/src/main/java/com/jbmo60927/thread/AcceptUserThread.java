package com.jbmo60927.thread;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;

/**
 * thread to accept new client and redirect them on other new thread
 */
public class AcceptUserThread extends Thread{

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(AcceptUserThread.class.getName());

    //number of clients connected during the server execution
    private int clientNumber = 0;

    //server socket open to accept connection from clients
    private final ServerSocket listener;

    //all data is stored in the app
    private final App app;

    /**
     * init data for the thread that accept new player
     * @param listener socket init
     * @param app link to the app
     */
    public AcceptUserThread(final ServerSocket listener, final App app) {
        LOGGER.setLevel(Level.INFO);
        this.listener = listener;
        this.app = app;
    }

    /**
     * the thread wait for a new connection on the port and creat a new serviceThread for each connection
     */
    @Override
    public void run() {
        try {
            while (!this.isInterrupted()) {
                //accept client connection request
                final Socket socketOfServer = listener.accept();

                //create new Socket at server
                new ServiceThread(socketOfServer, clientNumber++, app).start();
            }
        } catch (final Exception e) {
            if(!this.isInterrupted()) {
                LOGGER.log(Level.SEVERE, "Error while trying to accept new user", e);
            }
        }
    }
}
