package com.jbmo60927.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.packets.welcome_packet.ReceivedWelcomePacket;
import com.jbmo60927.packets.welcome_packet.SendWelcomePacket;
import com.jbmo60927.packets.Packet;
import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.packets.SendPacket;
import com.jbmo60927.packets.Packet.PacketType;
import com.jbmo60927.packets.join_packet.ReceivedJoinPacket;
import com.jbmo60927.packets.position_packet.ReceivedPositionPacket;
import com.jbmo60927.packets.quit_packet.ReceivedQuitPacket;
import com.jbmo60927.packets.reception_packet.ReceivedReceptionPacket;
import com.jbmo60927.packets.reception_packet.SendReceptionPacket;
import com.jbmo60927.packets.set_level_packet.SendSetLevelPacket;
import com.jbmo60927.packets.version_packet.ReceivedVersionPacket;
import com.jbmo60927.packets.version_packet.SendVersionPacket;
import static com.jbmo60927.utilz.HelpsMethods.bytesToInt;

public class ServiceThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ServiceThread.class.getName()); //logger for this class
    private final int clientNumber; //id of the client
    private final Socket socketOfServer; //socket of the client
    private final App app; //link to the data
    private final InputStream is; //input stream
    private final OutputStream os; //output stream

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

        //open input and output streams
        is = socketOfServer.getInputStream();
        os = socketOfServer.getOutputStream();

        sendPacket(new SendWelcomePacket());
        receiveInit(PacketType.WELCOME);
        sendPacket(new SendReceptionPacket());
        receiveInit(PacketType.VERSION);
        sendPacket(new SendVersionPacket(app, this));
        receiveInit(PacketType.RECEPTION);
        sendPacket(new SendSetLevelPacket(app.getLevelHandler().getLevel(0)));

    }

    /**
     * thread to receive and send data to the client
     */
    @Override
    public void run() {
        try {
            while (true) {
                
            }
        //error
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "error during the service thread", e);
        }
    }

    private int readPacketType() throws IOException {
        return bytesToInt(is.readNBytes(Packet.PACKETTYPEBYTES));
    }

    private int readPacketSize() throws IOException {
        return bytesToInt(is.readNBytes(Packet.PACKETSIZEBYTES));
    }

    private byte[] readPacketParameters(int packetSize) throws IOException {
        return is.readNBytes(packetSize);
    }

    /**
     * used to read a packet
     * @return a message that could be displayed in logs
     * @throws IOException if error with connection
     */
    private String receiveBase(boolean expectedType, byte packetTypeExpected) throws IOException {
        int packetType;
        int packetSize;
        byte[] parameters;
        do {
            packetType = readPacketType();
            packetSize = readPacketSize();
            parameters = readPacketParameters(packetSize);
            int logPacketType = packetType;
            int logPacketSize = packetSize;
            byte[] logParameters = parameters;
            LOGGER.log(Level.FINEST, () -> String.format("packet receive: %d %d %s", logPacketType, logPacketSize, new String(logParameters, StandardCharsets.UTF_8)));
        } while (expectedType && (packetType != packetTypeExpected));


        ReceivedPacket receivedPacket = null;
        switch (packetType) {
            case PacketType.RECEPTION:
                receivedPacket = new ReceivedReceptionPacket();
                break;
            case PacketType.WELCOME:
                receivedPacket = new ReceivedWelcomePacket(parameters);
                break;
            case PacketType.VERSION:
                receivedPacket = new ReceivedVersionPacket(parameters, app, this);
                break;
            case PacketType.JOIN:
                receivedPacket = new ReceivedJoinPacket();
                break;
            case PacketType.QUIT:
                receivedPacket = new ReceivedQuitPacket();
                break;
            case PacketType.POSITION:
                receivedPacket = new ReceivedPositionPacket();
                break;
            default:
                int logPacketType = packetType;
                int logPacketSize = packetSize;
                byte[] logParameters = parameters;
                LOGGER.log(Level.SEVERE, () -> String.format("packet type unknow%npacket type: %d%npacket size: %d%n parameters: %s", logPacketType, logPacketSize, new String(logParameters, StandardCharsets.UTF_8)));
                break;
        }

        //return the message
        if (receivedPacket != null) {
            receivedPacket.execute();
            return receivedPacket.getMessage();
        }
        return "";
    }


    /**
     * used to read a packet and display the message it return into logs
     * @throws IOException if error with connection
     */
    private void receive() throws IOException {
        String message = receiveBase(false, (byte)0);
        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.log(Level.INFO, message);
    }

    private void receiveInit(byte packetType) throws IOException {
        receiveBase(true, packetType);
    }
    

    /**
     * get the id of the client
     * @return the id
     */
    public int getClientNumber() {
        return clientNumber;
    }

    public OutputStream getOs() {
        return os;
    }

    /**
     * send a packet on a defined output stream
     * @param packet the packet to send
     * @param os the output stream to write on
     */
    private void sendPacket(SendPacket packet, OutputStream os) {
        try {
            os.write(packet.getPacket());
            os.flush();
            if (LOGGER.isLoggable(Level.FINEST))
                LOGGER.log(Level.FINEST, () -> String.format("packet send: %d %d %s", packet.getPacketType(), packet.getPacketSize(), new String(packet.getParameters(), StandardCharsets.UTF_8)));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error while sending packet", e);
        }
    }

    /**
     * send a packet to the output stream of the client
     * @param packet the packet to send
     */
    private void sendPacket(SendPacket packet) {
        sendPacket(packet, os);
    }

    /**
     * broadcast a paquet to every client
     * @param packet the packet to broadcast to evry client
     */
    private void broadcast(SendPacket packet) {
        //send data to every other clients
        for (final ServiceThread thread : app.getPlayers().keySet()) {
            if(thread != this) {
                sendPacket(packet, thread.getOs());
            }
        }
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }
}