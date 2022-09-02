package com.jbmo60927.display;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;

import com.jbmo60927.entities.Player;

public class ServiceThread extends Thread {

    private int clientNumber;
    private Socket socketOfServer;
    private Game game;
    private BufferedReader is;
    private BufferedWriter os;

    public ServiceThread(Socket socketOfServer, int clientNumber, Game game) throws IOException {
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;
        this.game = game;

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
        game.getPlayers().put(this, new Player(xPos, yPos, name));

        //send player parameters to all other players
        broadcast(String.format("NEWPLAYER %d %s", clientNumber, clientNumber+line.replace("INITPLAYER ", "")));

        for (ServiceThread thread : game.getPlayers().keySet()) {
            if(thread != this) {
                os.write(String.format("NEWPLAYER %d %s %s %s", thread.getClientNumber(), game.getPlayers().get(thread).getX(), game.getPlayers().get(thread).getY(), game.getPlayers().get(thread).getName()));
                os.newLine();
                os.flush();
            }
        }

        //send data to player
        os.write("INITDATA ");
        os.newLine();
        os.flush();

        //log connection

        Game.getLogger().log(Level.INFO, "New connection with client# {0} at {1} named {2}", new Object[] {this.clientNumber, socketOfServer, name});
    }

    private void broadcast(String trame) throws IOException {
        //send data to evry other player
        for (ServiceThread thread : game.getPlayers().keySet()) {
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
                if (line.split(" ")[0].compareTo("PLAYER") == 0) {
                    game.getPlayers().get(this).setX(Float.parseFloat(line.split(" ")[1]));
                    game.getPlayers().get(this).setY(Float.parseFloat(line.split(" ")[2]));
                    game.getPlayers().get(this).setPlayerAction(Integer.parseInt(line.split(" ")[3]));
                    broadcast(String.format("PLAYER %d %s", clientNumber, line.replace("PLAYER ", "")));

                // If users send QUIT (To end conversation).
                } else if (line.equals("QUIT")) {
                    game.getPlayers().remove(this);
                    broadcast(String.format("REMOVEPLAYER %d", clientNumber));

                    Game.getLogger().log(Level.INFO, "Connection stop with client# {0} at {1}", new Object[] {this.clientNumber, socketOfServer});
                    os.write("OK");
                    os.newLine();
                    os.flush();
                    break;
                
                //else display command
                } else {
                    Game.getLogger().log(Level.INFO, line);
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