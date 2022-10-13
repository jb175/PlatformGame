package com.jbmo60927.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.entities.Player;

public class ServiceThread extends Thread {

    private int clientNumber;
    private Socket socketOfServer;
    private App app;
    private BufferedReader is;
    private BufferedWriter os;

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(ServiceThread.class.getName());

    public ServiceThread(Socket socketOfServer, int clientNumber, App app) throws IOException {
        LOGGER.setLevel(Level.INFO);
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;
        this.app = app;

        // Open input and output streams
        is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

        
        //receive data from player
        String line;
        do {
            line = is.readLine();
        } while (line.split(" ")[0].compareTo("INITPLAYER") != 0);
        Float xPos = Float.parseFloat(line.split(" ")[1]);
        Float yPos = Float.parseFloat(line.split(" ")[2]);
        String name = line.split(" ")[3];
        //add player
        app.getPlayers().put(this, new Player(xPos, yPos, name));

        //send player parameters to all other players
        broadcast(String.format("NEWPLAYER %d %s", clientNumber, clientNumber+line.replace("INITPLAYER ", "")));

        for (ServiceThread thread : app.getPlayers().keySet()) {
            if(thread != this) {
                os.write(String.format("NEWPLAYER %d %s %s %s", thread.getClientNumber(), app.getPlayers().get(thread).getX(), app.getPlayers().get(thread).getY(), app.getPlayers().get(thread).getName()));
                os.newLine();
                os.flush();
            }
        }

        //send data to player
        os.write("INITDATA ");
        os.newLine();
        os.flush();

        //log connection

        LOGGER.log(Level.INFO, "New connection with client# {0} at {1} named {2}", new Object[] {this.clientNumber, socketOfServer, name});
    }

    private void broadcast(String trame) throws IOException {
        //send data to evry other player
        for (ServiceThread thread : app.getPlayers().keySet()) {
            if(thread != this) {
                thread.getOs().write(trame);
                thread.getOs().newLine();
                thread.getOs().flush();
            }
        }
    }

    @Override
    public void run() {

        try {

            while (true) {

                // Read data to the server (sent from client).
                String line = is.readLine();
                LOGGER.log(Level.FINE, "paquet received: {}", line);

                if (line.split(" ")[0].compareTo("PLAYER") == 0) {
                    app.getPlayers().get(this).setX(Float.parseFloat(line.split(" ")[1]));
                    app.getPlayers().get(this).setY(Float.parseFloat(line.split(" ")[2]));
                    app.getPlayers().get(this).setPlayerAction(Integer.parseInt(line.split(" ")[3]));
                    broadcast(String.format("PLAYER %d %s", clientNumber, line.replace("PLAYER ", "")));

                // If users send QUIT (To end conversation).
                } else if (line.equals("QUIT")) {
                    app.getPlayers().remove(this);
                    broadcast(String.format("REMOVEPLAYER %d", clientNumber));

                    LOGGER.log(Level.INFO, "Connection stop with client# {0} at {1}", new Object[] {this.clientNumber, socketOfServer});
                    os.write("OK");
                    os.newLine();
                    os.flush();
                    break;
                
                //else display command
                } else {
                    LOGGER.log(Level.INFO, line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedWriter getOs() {
        return os;
    }

    public int getClientNumber() {
        return clientNumber;
    }
}