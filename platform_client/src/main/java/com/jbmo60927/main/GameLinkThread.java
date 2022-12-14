package com.jbmo60927.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.packets.Packet;
import com.jbmo60927.packets.Packet.PacketType;
import com.jbmo60927.packets.join_packet.SendJoinPacket;
import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.packets.SendPacket;
import com.jbmo60927.packets.new_entity_packet.ReceivedNewEntityPacket;
import com.jbmo60927.packets.new_joiner_packet.ReceivedNewJoinerPacket;
import com.jbmo60927.packets.position_packet.ReceivedPositionPacket;
import com.jbmo60927.packets.quit_packet.ReceivedQuitPacket;
import com.jbmo60927.packets.reception_packet.ReceivedReceptionPacket;
import com.jbmo60927.packets.reception_packet.SendReceptionPacket;
import com.jbmo60927.packets.remove_level_packet.ReceivedRemoveLevelPacket;
import com.jbmo60927.packets.remove_player_packet.ReceivedRemovePlayerPacket;
import com.jbmo60927.packets.set_level_packet.ReceivedSetLevelPacket;
import com.jbmo60927.packets.version_packet.ReceivedVersionPacket;
import com.jbmo60927.packets.version_packet.SendVersionPacket;
import com.jbmo60927.packets.welcome_packet.ReceivedWelcomePacket;
import com.jbmo60927.packets.welcome_packet.SendWelcomePacket;

import static com.jbmo60927.utilz.HelpsMethods.bytesToInt;

public class GameLinkThread extends Thread {

    private Socket socketOfClient;
    private App app;

    private final InputStream is; //input stream
    private final OutputStream os; //output stream

    private static final Logger LOGGER = Logger.getLogger(GameLinkThread.class.getName());
    
    public GameLinkThread(Socket socketOfClient, App app) throws IOException {
        LOGGER.setLevel(Level.FINEST);

        this.socketOfClient = socketOfClient;
        this.app = app;

        is = socketOfClient.getInputStream();
        os = socketOfClient.getOutputStream();


        receiveInit(PacketType.WELCOME);
        sendPacket(new SendWelcomePacket());
        receiveInit(PacketType.RECEPTION);
        sendPacket(new SendVersionPacket());
        receiveInit(PacketType.VERSION);
        sendPacket(new SendJoinPacket(app.getPlaying().getPlayer()));
        receveMultipleInit(PacketType.SETLEVEL);
        LOGGER.log(Level.INFO, String.format("%d levels have been received", app.getPlaying().getLevelHandler().getLevels().length));
        sendPacket(new SendReceptionPacket());
        receveMultipleInit(PacketType.NEWJOINER);
    }

    @Override
    public void run() {
        try {
            while (!this.isInterrupted()) {
                receive();
            }
        //error
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "error during the game link thread");
        }
    }

    private static int readPacketType(InputStream is) throws IOException {
        return bytesToInt(is.readNBytes(Packet.PACKETTYPEBYTES));
    }

    private static int readPacketSize(InputStream is) throws IOException {
        return bytesToInt(is.readNBytes(Packet.PACKETSIZEBYTES));
    }

    private static byte[] readPacketParameters(InputStream is, int packetSize) throws IOException {
        return is.readNBytes(packetSize);
    }

    /**
     * used to read a packet
     * @return a message that could be displayed in logs
     * @throws IOException if error with connection
     */
    private static String receiveBase(boolean expectedType, boolean untilReception, byte packetTypeExpected, InputStream is, App app) throws IOException {
        int packetType;
        int packetSize;
        byte[] parameters;
        do {
            packetType = readPacketType(is);
            packetSize = readPacketSize(is);
            parameters = readPacketParameters(is, packetSize);
            int logPacketType = packetType;
            int logPacketSize = packetSize;
            byte[] logParameters = parameters;
            LOGGER.log(Level.FINEST, () -> String.format("packet receive: %d %d %s", logPacketType, logPacketSize, new String(logParameters, StandardCharsets.UTF_8)));
            if(untilReception && packetType == PacketType.RECEPTION)
                return null;
        } while (expectedType && (packetType != packetTypeExpected));


        ReceivedPacket receivedPacket = null;
        switch (packetType) {
            case 0:
                LOGGER.log(Level.SEVERE, "empty packet");
                break;
            case PacketType.RECEPTION:
                receivedPacket = new ReceivedReceptionPacket();
                break;
            case PacketType.WELCOME:
                receivedPacket = new ReceivedWelcomePacket(parameters);
                break;
            case PacketType.VERSION:
                receivedPacket = new ReceivedVersionPacket(parameters);
                break;
            case PacketType.QUIT:
                receivedPacket = new ReceivedQuitPacket(app);
                break;
            case PacketType.NEWJOINER:
                receivedPacket = new ReceivedNewJoinerPacket(parameters, app);
                break;
            case PacketType.POSITION:
                receivedPacket = new ReceivedPositionPacket();
                break;
            case PacketType.REMOVEPLAYER:
                receivedPacket = new ReceivedRemovePlayerPacket(parameters, app);
                break;
            case PacketType.NEWENTITY:
                receivedPacket = new ReceivedNewEntityPacket(parameters, app);
                break;
            case PacketType.SETLEVEL:
                receivedPacket = new ReceivedSetLevelPacket(parameters, app);
                break;
            case PacketType.REMOVELEVEL:
                receivedPacket = new ReceivedRemoveLevelPacket(parameters, app);
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
    public void receive() throws IOException {
        String message = receiveBase(false, false, (byte)0, is, app);
        if (LOGGER.isLoggable(Level.INFO) && !"".equals(message))
            LOGGER.log(Level.INFO, message);
    }

    public void receiveInit(byte packetType) throws IOException {
        receiveBase(true, false, packetType, is, app);
    }

    public void receveMultipleInit(byte packetType) throws IOException {
        String message;
        do {
            message = receiveBase(true, true, packetType, is, app);
        } while (message != null);
    }

    private static void sendPacket(SendPacket packet, OutputStream os) {
        try {
            os.write(packet.getPacket());
            os.flush();
            if (LOGGER.isLoggable(Level.FINEST))
                LOGGER.log(Level.FINEST, () -> String.format("packet send: %d %d %s", packet.getPacketType(), packet.getPacketSize(), new String(packet.getParameters(), StandardCharsets.UTF_8)));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error while sending packet", e);
        }
    }

    public void sendPacket(SendPacket packet) {
        sendPacket(packet, os);
    }

    public void sendMultiple(SendPacket[] packets) {
        for (SendPacket sendPacket : packets) {
            sendPacket(sendPacket, os);
        }
        sendPacket(new SendReceptionPacket(), os);
    }
}
