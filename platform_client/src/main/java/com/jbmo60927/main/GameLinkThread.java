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
import com.jbmo60927.packets.reception_packet.SendReceptionPacket;
import com.jbmo60927.packets.remove_player_packet.ReceivedRemovePlayerPacket;
import com.jbmo60927.packets.version_packet.ReceivedVersionPacket;
import com.jbmo60927.packets.version_packet.SendVersionPacket;
import com.jbmo60927.packets.welcome_packet.ReceivedWelcomePacket;
import com.jbmo60927.packets.Packet.PacketType;
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

        //receive data from server
        receive((byte) PacketType.WELCOME);

        sendPacket(new SendVersionPacket().toString());
        
        receive((byte) PacketType.VERSION);

        sendPacket(new SendReceptionPacket(true).toString());

        receive((byte) PacketType.LEVEL);

    }

    private void receive(byte packetType) throws IOException {
        String line;
        do {
            line = is.readLine();
        } while (line.getBytes()[0] != packetType);
        createPacket(line);
    }

    @Override
    public void run() {
        String line;
        try {
            while (true) {
                    line = is.readLine();
                LOGGER.log(Level.INFO, line);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "error during the thread", e);
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
                new ReceivedLevelPacket(parameters).execute();
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
