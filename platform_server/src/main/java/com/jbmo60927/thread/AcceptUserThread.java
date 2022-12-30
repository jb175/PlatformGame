package com.jbmo60927.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

import com.jbmo60927.App;
import com.jbmo60927.communication.packets.Packet;

/**
 * thread to accept new client and redirect them on other new thread
 */
public class AcceptUserThread extends Thread{

    private static int MAXPLAYER = 10;

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(AcceptUserThread.class.getName());

    //number of clients connected during the server execution
    private int clientNumber = 0;

    //server socket open to accept connection from clients
    private ServerSocket listener;

    //all data is stored in the app
    private final App app;

    private ServiceThread[] st = new ServiceThread[MAXPLAYER];

    /**
     * init data for the thread that accept new player
     * @param listener socket init
     * @param app link to the app
     */
    public AcceptUserThread(ServerSocket listener, final App app) {
        LOGGER.setLevel(Level.INFO);
        this.listener = listener;
        this.app = app;
        LOGGER.log(Level.FINE, "thread to accept new client corectly initialized");
    }

    /**
     * the thread wait for a new connection on the port and creat a new serviceThread for each connection
     */
    @Override
    public void run() {
        LOGGER.log(Level.INFO, "thread that accept new client is running");
        Socket socketOfServer;
        while (!this.isInterrupted()) {
            try {
                //accept client connection request
                socketOfServer = listener.accept();

                LOGGER.log(Level.FINE, "new connection detected");

                for (int i = 0; i < st.length; i++) {
                    if (st[i] == null || st[i].isInterrupted()) {
                        //create new Socket at server
                        st[i] = new ServiceThread(socketOfServer, clientNumber++, app);
                        break;
                    } else if (i == (st.length-1)) {
                        socketOfServer.getOutputStream().write(stringToBytes("server full"));
                        socketOfServer.getOutputStream().flush();
                        socketOfServer.close();
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error while trying to accept new user", e);
            }

        }
        LOGGER.log(Level.SEVERE, "AcceptUserThread stopped");
    }

    public ServiceThread[] getSt() {
        return st;
    }

    public void broadcast(Packet packet) {
        for (ServiceThread serviceThread : st) {
            if (serviceThread != null && !serviceThread.isInterrupted()) {
                serviceThread.getSendPacket().sendPacket(packet);
            }
        }
    }
}
