package com.jbmo60927.thread;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.communication.packets.Packet;
import com.jbmo60927.entities.ServerPlayer;

public class ServiceThread {

    private static final Logger LOGGER = Logger.getLogger(ServiceThread.class.getName()); //logger for this class
    private final int clientNumber; //id of the client
    private final Socket socketOfServer; //socket of the client
    private final App app; //link to the data
    private final OutputStream os;
    private final ReceivePacketThread receveivePacket;
    private final SendPacketThread sendPacket;
    private final int playerListNumber;


    private String clientVersion = "";

    /**
     * thread to communicate with a unique client
     * @param socketOfServer socket of the client
     * @param clientNumber id of the client
     * @param app link to the data
     * @param playerListNumber the position of the player in the list from AcceptUserThread
     * @throws IOException exception that could occure when the communication is closed badly 
     */
    public ServiceThread(final Socket socketOfServer, final int clientNumber, final App app, final int playerListNumber) throws IOException {
        LOGGER.setLevel(Level.FINEST);
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;
        this.app = app;
        this.playerListNumber = playerListNumber;
        this.os = socketOfServer.getOutputStream();
        this.receveivePacket = new ReceivePacketThread(socketOfServer, this.app, this);
        this.sendPacket = new SendPacketThread(socketOfServer, this.app);
        receveivePacket.start();
    }
    

    /**
     * get the id of the client
     * @return the id
     */
    public int getClientNumber() {
        return clientNumber;
    }

    /**
     * to allow broadcast for packet
     * @return
     */
    public OutputStream getOs() {
        return os;
    }

    /**
     * broadcast a paquet to every other clients
     * @param packet the packet to broadcast to evry clients
     */
    public void broadcast(Packet packet) {
        for (ServerPlayer player : app.getAcceptUserThread().getPlayers()) {
            if (player != null && !player.getServiceThread().isInterrupted() && !player.getServiceThread().equals(this)) {
                player.getServiceThread().getSendPacket().sendPacket(packet);
            }
        }
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public ReceivePacketThread getReceveivePacket() {
        return receveivePacket;
    }

    public SendPacketThread getSendPacket() {
        return sendPacket;
    }

    public int getPlayerListNumber() {
        return playerListNumber;
    }

    public void interrupt() {
        try {
            if (!this.socketOfServer.isClosed())
                socketOfServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!this.receveivePacket.isInterrupted())
            this.receveivePacket.interrupt();
    }

    public Boolean isInterrupted() {
        return socketOfServer.isClosed() || this.receveivePacket.isInterrupted();
    }
}