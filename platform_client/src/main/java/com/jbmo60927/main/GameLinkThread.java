package com.jbmo60927.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.entities.OtherPlayer;
import com.jbmo60927.App;

public class GameLinkThread extends Thread {

    private Socket socketOfClient;
    private App app;

    private BufferedWriter os;
    private BufferedReader is;

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    
    public GameLinkThread(Socket socketOfClient, App app) throws IOException {
        LOGGER.setLevel(Level.INFO);

        this.socketOfClient = socketOfClient;
        this.app = app;

        is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));

        os.write("");
        os.newLine();
        os.flush();

        Boolean test = true;
        while (test) {

        }

        //send data to server
        os.write(String.format("INITPLAYER %s %s %s", app.getPlaying().getPlayer().getX(), app.getPlaying().getPlayer().getY(), app.getPlaying().getPlayer().getName()));
        os.newLine();
        os.flush();

        //receive data from server
        String line;
        do {
            line = is.readLine();
            if(line.split(" ")[0].compareTo("NEWPLAYER") == 0)
                app.getPlaying().getPlayers().put(Integer.parseInt(line.split(" ")[1]), new OtherPlayer(Float.parseFloat(line.split(" ")[2]), Float.parseFloat(line.split(" ")[3]), line.split(" ")[4]));
        } while (line.split(" ")[0].compareTo("INITDATA") != 0);
    }

    private void sendData(String trame) {
        try {
            os.write(trame);
            os.newLine();
            os.flush();
        } catch (IOException e) {
            if(!("Stream closed".equals(e.getMessage()) || "Socket closed".equals(e.getMessage()))) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    @Override
    public void run() {
        try {
            // Open input and output streams

            while (true) {
                String line = is.readLine();
                LOGGER.log(Level.FINEST, () -> String.format("new paquet: %s", line));

                if (line.split(" ")[0].compareTo("PLAYER") == 0) {
                    updatePlayerPosition(line);
                } else if(line.split(" ")[0].compareTo("NEWPLAYER") == 0) {
                    LOGGER.log(Level.INFO, () -> String.format("%s joined the server", line.split(" ")[4]));
                    app.getPlaying().getPlayers().put(Integer.parseInt(line.split(" ")[1]), new OtherPlayer(Float.parseFloat(line.split(" ")[2]), Float.parseFloat(line.split(" ")[3]), line.split(" ")[4]));
                } else if(line.split(" ")[0].compareTo("REMOVEPLAYER") == 0) {
                    LOGGER.log(Level.INFO, () -> String.format("%s leave the server", app.getPlaying().getPlayers().get(Integer.parseInt(line.split(" ")[1]))));
                    app.getPlaying().getPlayers().remove(Integer.parseInt(line.split(" ")[1]));

                } else if(line.equals("OK")) {
                    LOGGER.log(Level.INFO, "you leave the server (1/5)");
                    //stop the connection
                    is.close();
                    LOGGER.log(Level.INFO, "you leave the server (2/5)");
                    os.close();
                    LOGGER.log(Level.INFO, "you leave the server (3/5)");
                    socketOfClient.close();
                    LOGGER.log(Level.INFO, "you leave the server (4/5)");
                    app.getConnect().setConnected(false);
                    LOGGER.log(Level.INFO, "you leave the server (5/5)");

                } else {
                    LOGGER.log(Level.INFO, line);
                }
            }

        } catch (IOException e) {
            if(!("Stream closed".equals(e.getMessage()) || "Socket closed".equals(e.getMessage())))
                e.printStackTrace();
        }
    }

    private void updatePlayerPosition(String line) {
        try {
            app.getPlaying().getPlayers().get(Integer.parseInt(line.split(" ")[1])).setPosition(Float.parseFloat(line.split(" ")[2]), Float.parseFloat(line.split(" ")[3]));
            app.getPlaying().getPlayers().get(Integer.parseInt(line.split(" ")[1])).setPlayerAction(Integer.parseInt(line.split(" ")[4]));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error while receiving player position", e);
        }
    }

    public void sendUpdates() {
        sendData("PLAYER "+app.getPlaying().getPlayer().getData());
    }

    public void close() {
        sendData("QUIT");
    }
}
