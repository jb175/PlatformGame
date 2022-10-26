package com.jbmo60927.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.entities.OtherPlayer;
import com.jbmo60927.packets.level_packet.ReceivedLevelPacket;
import com.jbmo60927.packets.new_joiner_packet.ReceivedNewJoinerPacket;
import com.jbmo60927.packets.new_player_packet.ReceivedNewPlayerPacket;
import com.jbmo60927.packets.position_packet.ReceivedPositionPacket;
import com.jbmo60927.packets.remove_player_packet.ReceivedRemovePlayerPacket;
import com.jbmo60927.packets.version_packet.ReceivedVersionPacket;
import com.jbmo60927.packets.version_packet.SendVersionPacket;
import com.jbmo60927.packets.welcome_packet.ReceivedWelcomePacket;
import com.jbmo60927.utilz.Constants.PacketType;
import com.jbmo60927.App;

public class GameLinkThread extends Thread {

    private Socket socketOfClient;
    private App app;

    private BufferedWriter os;
    private BufferedReader is;

    private static final Logger LOGGER = Logger.getLogger(GameLinkThread.class.getName());
    
    public GameLinkThread(Socket socketOfClient, App app) throws IOException {
        LOGGER.setLevel(Level.INFO);

        this.socketOfClient = socketOfClient;
        this.app = app;

        is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));

        String line;
        //receive data from server
        do {
            line = is.readLine();
        } while (line.getBytes()[0] != (byte) PacketType.WELCOME);
        createPacket(line);

        sendPacket((new SendVersionPacket()).toString());
        
        do {
            line = is.readLine();
        } while (line.getBytes()[0] != (byte) PacketType.VERSION);
        createPacket(line);

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
            app.getPlaying().getPlayers().get(Integer.parseInt(line.split(" ")[1])).setPosition(Float.parseFloat(line.split(" ")[2])*App.SCALE, Float.parseFloat(line.split(" ")[3])*App.SCALE);
            app.getPlaying().getPlayers().get(Integer.parseInt(line.split(" ")[1])).setPlayerAction(Integer.parseInt(line.split(" ")[4]));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error while receiving player position", e);
        }
    }

    public void sendUpdates() {
        sendPacket("PLAYER "+app.getPlaying().getPlayer().getData());
    }

    public void close() {
        sendPacket("QUIT");
        LOGGER.log(Level.INFO, "close request send to server");
        //stop the connection
        try {
            is.close();
            os.close();
            socketOfClient.close();
            app.getConnect().setConnected(false);;
            LOGGER.log(Level.INFO, "you leave the server");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void sendPacket(String packet) {
        try {
            os.write(packet);
            os.newLine();
            os.flush();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error while sending packet", e);
        }
    }

    public static void createPacket(String data) {
        byte[] byteArray = data.getBytes(StandardCharsets.UTF_8);
        byte[] parameters = new byte[byteArray.length-1];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = byteArray[i+1];
        }
        switch (byteArray[0]) {
            case PacketType.WELCOME:
                new ReceivedWelcomePacket(parameters).execute();
                break;
            case PacketType.VERSION:
                new ReceivedVersionPacket(parameters).execute();
                break;
            case PacketType.LEVEL:
                new ReceivedLevelPacket(parameters);
                break;
            case PacketType.NEWJOINER:
                new ReceivedNewJoinerPacket(parameters);
                break;
            case PacketType.NEWPLAYER:
                new ReceivedNewPlayerPacket(parameters);
                break;
            case PacketType.POSITION:
                new ReceivedPositionPacket(parameters);
                break;
            case PacketType.REMOVEPLAYER:
                new ReceivedRemovePlayerPacket(parameters);
                break;
            default:
                LOGGER.log(Level.SEVERE, "a packet has been received and allow to be interpreted but not recognize by the function");
                break;
        }
    }
}
