package com.jbmo60927.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.command.Command;

public class ServiceThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ServiceThread.class.getName()); //logger for this class
    private final int clientNumber; //id of the client
    private final Socket socketOfServer; //socket of the client
    private final App app; //link to the data
    private final OutputStream os;
    private final ReceivePacketThread receveivePacket;
    private final SendPacketThread sendPacket;


    private String clientVersion = "";

    /**
     * thread to communicate with a unique client
     * @param socketOfServer socket of the client
     * @param clientNumber id of the client
     * @param app link to the data
     * @throws IOException exception that could occure when the communication is closed badly 
     */
    public ServiceThread(final Socket socketOfServer, final int clientNumber, final App app) throws IOException {
        LOGGER.setLevel(Level.FINEST);
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;
        this.app = app;
        this.os = socketOfServer.getOutputStream();
        this.receveivePacket = new ReceivePacketThread(socketOfServer);
        this.sendPacket = new SendPacketThread(socketOfServer);
        receveivePacket.start();
        sendPacket.start();
    }

    public void run() {
        while (true) {
        }
    }

    // /**
    //  * used to read a packet
    //  * @return a message that could be displayed in logs
    //  * @throws IOException if error with connection
    //  */
    // private static String receiveBase(boolean expectedType, boolean untilReception, byte packetTypeExpected, InputStream is, App app, ServiceThread st) throws IOException {
    //     int packetType;
    //     int packetSize;
    //     byte[] parameters;
    //     do {
    //         packetType = readPacketType(is);
    //         packetSize = readPacketSize(is);
    //         parameters = readPacketParameters(is, packetSize);
    //         int logPacketType = packetType;
    //         int logPacketSize = packetSize;
    //         byte[] logParameters = parameters;
    //         LOGGER.log(Level.FINEST, () -> String.format("packet receive: %d %d %s", logPacketType, logPacketSize, new String(logParameters, StandardCharsets.UTF_8)));
    //         if(untilReception && packetType == PacketType.RECEPTION)
    //             return null;
    //     } while (expectedType && (packetType != packetTypeExpected));


    //     ReceivedPacket receivedPacket = null;
    //     switch (packetType) {
    //         case 0:
    //             LOGGER.log(Level.SEVERE, "empty packet", new IOException());
    //             break;
    //         case PacketType.RECEPTION:
    //             receivedPacket = new ReceivedReceptionPacket();
    //             break;
    //         case PacketType.WELCOME:
    //             receivedPacket = new ReceivedWelcomePacket(parameters);
    //             break;
    //         case PacketType.VERSION:
    //             receivedPacket = new ReceivedVersionPacket(parameters, app, st);
    //             break;
    //         case PacketType.JOIN:
    //             receivedPacket = new ReceivedJoinPacket(parameters, app);
    //             break;
    //         case PacketType.QUIT:
    //             receivedPacket = new ReceivedQuitPacket(parameters, app, st);
    //             break;
    //         case PacketType.POSITION:
    //             receivedPacket = new ReceivedPositionPacket();
    //             break;
    //         default:
    //             int logPacketType = packetType;
    //             int logPacketSize = packetSize;
    //             byte[] logParameters = parameters;
    //             LOGGER.log(Level.SEVERE, () -> String.format("packet type unknow%npacket type: %d%npacket size: %d%n parameters: %s", logPacketType, logPacketSize, new String(logParameters, StandardCharsets.UTF_8)));
    //             break;
    //     }

    //     //return the message
    //     if (receivedPacket != null) {
    //         receivedPacket.execute();
    //         return receivedPacket.getMessage();
    //     }
    //     return "";
    // }

    // /**
    //  * used to read a packet and display the message it return into logs
    //  * @throws IOException if error with connection
    //  */
    // public void receive() throws IOException {
    //     String message = receiveBase(false, false, (byte)0, is, app, this);
    //     if (LOGGER.isLoggable(Level.INFO) && !"".equals(message))
    //         LOGGER.log(Level.INFO, message);
    // }

    // public void receiveInit(byte packetType) throws IOException {
    //     receiveBase(true, false, packetType, is, app, this);
    // }

    // public void receveMultipleInit(byte packetType) throws IOException {
    //     String message;
    //     do {
    //         message = receiveBase(true, true, packetType, is, app, this);
    //     } while (message != null);
    // }

    // private static void sendPacket(SendPacket packet, OutputStream os) {
    //     try {
    //         os.write(packet.getPacket());
    //         os.flush();
    //         if (LOGGER.isLoggable(Level.FINEST))
    //             LOGGER.log(Level.FINEST, () -> String.format("packet send: %d %d %s", packet.getPacketType(), packet.getPacketSize(), new String(packet.getParameters(), StandardCharsets.UTF_8)));
    //     } catch (Exception e) {
    //         LOGGER.log(Level.SEVERE, "error while sending packet", e);
    //     }
    // }

    // public void sendPacket(SendPacket packet) {
    //     sendPacket(packet, os);
    // }

    // public void sendMultiple(SendPacket[] packets) {
    //     for (SendPacket sendPacket : packets) {
    //         sendPacket(sendPacket, os);
    //     }
    //     sendPacket(new SendReceptionPacket(), os);
    // }
    

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
     * broadcast a paquet to every client
     * @param packet the packet to broadcast to evry client
     */
    // private void broadcast(Packet packet) {
    //     //send data to every other clients (evry thread)
    //     for (final ServiceThread thread : app.getPlayers().keySet()) {
    //         if(thread != this) {
    //             sendPacket(packet, thread.getOs());
    //         }
    //     }
    // }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    @Override
    public void interrupt() {
        try {
            socketOfServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.interrupt();
    }
}