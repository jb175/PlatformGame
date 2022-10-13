package com.jbmo60927.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.jbmo60927.entities.OtherPlayer;
import com.jbmo60927.App;

public class GameLinkThread extends Thread {

    private Socket socketOfClient;
    private App app;

    private BufferedWriter os;
    private BufferedReader is;

    public GameLinkThread(Socket socketOfClient, App app) throws IOException {
        this.socketOfClient = socketOfClient;
        this.app = app;

        is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));

        //send data to server
        os.write(String.format("INITPLAYER %s %s %s", this.app.getPlaying().getPlayer().getX(), this.app.getPlaying().getPlayer().getY(), this.app.getPlaying().getPlayer().getName()));
        os.newLine();
        os.flush();

        //receive data from server
        String line;
        do {
            line = is.readLine();
            if(line.split(" ")[0].compareTo("NEWPLAYER") == 0)
                this.app.getPlaying().getPlayers().put(Integer.parseInt(line.split(" ")[1]), new OtherPlayer(Float.parseFloat(line.split(" ")[2]), Float.parseFloat(line.split(" ")[3]), line.split(" ")[4]));
        } while (line.split(" ")[0].compareTo("INITDATA") != 0);
    }

    private void sendData(String trame) {
        try {
            os.write(trame);
            os.newLine();
            os.flush();
        } catch (IOException e) {
            if(!(e.getMessage() == "Stream closed" || e.getMessage() == "Socket closed")) {
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

                if (line.split(" ")[0].compareTo("PLAYER") == 0) {
                    try {
                        this.app.getPlaying().getPlayers().get(Integer.parseInt(line.split(" ")[1])).setPosition(Float.parseFloat(line.split(" ")[2]), Float.parseFloat(line.split(" ")[3]));
                        this.app.getPlaying().getPlayers().get(Integer.parseInt(line.split(" ")[1])).setPlayerAction(Integer.parseInt(line.split(" ")[4]));;
                    } catch (Exception e) {
                    }
                } else if(line.split(" ")[0].compareTo("NEWPLAYER") == 0) {
                    this.app.getPlaying().getPlayers().put(Integer.parseInt(line.split(" ")[1]), new OtherPlayer(Float.parseFloat(line.split(" ")[2]), Float.parseFloat(line.split(" ")[3]), line.split(" ")[4]));
                } else if(line.split(" ")[0].compareTo("REMOVEPLAYER") == 0) {
                    this.app.getPlaying().getPlayers().remove(Integer.parseInt(line.split(" ")[1]));
                } else if(line.equals("OK")) {
                    //stop the connection
                    is.close();
                    os.close();
                    socketOfClient.close();
                    this.app.getConnect().setConnected(false);

                } else {
                    System.out.println(line);
                }
            }

        } catch (IOException e) {
            if(!(e.getMessage() == "Stream closed" || e.getMessage() == "Socket closed"))
                e.printStackTrace();
        }
    }

    public void sendUpdates() {
        sendData("PLAYER "+this.app.getPlaying().getPlayer().getData());
    }

    public void close() {
        sendData("QUIT");
    }
}
