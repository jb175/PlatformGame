package com.jbmo60927.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.packets.new_joiner_packet.ReceivedNewJoinerPacket;
import com.jbmo60927.packets.position_packet.ReceivedPositionPacket;
import com.jbmo60927.packets.quit_packet.ReceivedQuitPacket;
import com.jbmo60927.packets.version_packet.ReceivedVersionPacket;
import com.jbmo60927.packets.version_packet.SendVersionPacket;
import com.jbmo60927.packets.welcome_packet.SendWelcomePacket;
import com.jbmo60927.utilz.Constants.PacketType;

public class ServiceThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ServiceThread.class.getName()); //logger for this class
    private final int clientNumber; //id of the client
    private final Socket socketOfServer; //socket of the client
    private final App app; //link to the data
    private final BufferedReader is; //input stream
    private final BufferedWriter os; //output stream

    private String clientVersion = "";

    /**
     * thread to communicate with a unique client
     * @param socketOfServer socket of the client
     * @param clientNumber id of the client
     * @param app link to the data
     * @throws IOException exception that could occure when the communication is closed badly 
     */
    public ServiceThread(final Socket socketOfServer, final int clientNumber, final App app) throws IOException {
        LOGGER.setLevel(Level.INFO);
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;
        this.app = app;

        //open input and output streams
        is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

        String line;

        sendPacket((new SendWelcomePacket()).toString());

        do {
            line = is.readLine();
        } while (line.getBytes()[0] != (byte) PacketType.VERSION);
        createPacket(line);

        sendPacket((new SendVersionPacket(app, this)).toString());

    }

    /**
     * thread to receive and send data to the client
     */
    @Override
    public void run() {
        try {
            while (true) {

                //read data to the server (sent from client).
                final String line = is.readLine();

                //log the paquet
                LOGGER.log(Level.FINEST, () -> String.format("paquet received: %s", line));

                //new position for a player
                if (line.split(" ")[0].compareTo("PLAYER") == 0) {
                    app.getPlayers().get(this).setX(Float.parseFloat(line.split(" ")[1]));
                    app.getPlayers().get(this).setY(Float.parseFloat(line.split(" ")[2]));
                    app.getPlayers().get(this).setPlayerAction(Integer.parseInt(line.split(" ")[3]));
                    broadcast(String.format("PLAYER %d %s", clientNumber, line.replace("PLAYER ", "")));

                //if users send QUIT (To end conversation).
                } else if (line.equals("QUIT")) {
                    app.getPlayers().remove(this);
                    broadcast(String.format("REMOVEPLAYER %d", clientNumber));

                    is.close();
                    os.close();
                    LOGGER.log(Level.INFO, () -> String.format("Connection stop with client# %d at %s", this.clientNumber, this.socketOfServer.getInetAddress().toString().replace("/", "")));

                    break;
                
                //else display command
                } else {
                    LOGGER.log(Level.INFO, line);
                }
            }

        //error
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "error during the service thread", e);
        }
    }

    /**
     * allow to write output from outside of the thread
     * @return the buffer to write
     */
    public BufferedWriter getOs() {
        return os;
    }

    /**
     * get the id of the client
     * @return the id
     */
    public int getClientNumber() {
        return clientNumber;
    }

    /**
     * broadcast a paquet to every client
     * @param trame the paquet to broadcast
     * @throws IOException exception that could occure if something is wrong with the connection
     */
    private void broadcast(final String trame) throws IOException {
        //send data to every other clients
        for (final ServiceThread thread : app.getPlayers().keySet()) {
            if(thread != this) {
                thread.getOs().write(trame);
                thread.getOs().newLine();
                thread.getOs().flush();
            }
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

    public void createPacket(String data){
        byte[] byteArray = data.getBytes(StandardCharsets.UTF_8);
        byte[] parameters = new byte[byteArray.length-1];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = byteArray[i+1];
        }
        switch (byteArray[0]) {
            case PacketType.VERSION:
                new ReceivedVersionPacket(parameters, app, this).execute();
                break;
            case PacketType.NEWJOINER:
                new ReceivedNewJoinerPacket(parameters).execute();
                break;
            case PacketType.POSITION:
                new ReceivedPositionPacket(parameters).execute();
                break;
            case PacketType.QUIT:
                new ReceivedQuitPacket(parameters).execute();
                break;
            default:
                LOGGER.log(Level.SEVERE, "a packet has been received and allow to be interpreted but not recognize by the function");
                break;
        }
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }
}