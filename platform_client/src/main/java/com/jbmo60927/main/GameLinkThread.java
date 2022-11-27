package com.jbmo60927.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.jbmo60927.packets.Packet;
import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.packets.SendPacket;
import com.jbmo60927.packets.Packet.PacketType;
import com.jbmo60927.packets.new_entity_packet.ReceivedNewEntityPacket;
import com.jbmo60927.packets.new_joiner_packet.ReceivedNewJoinerPacket;
import com.jbmo60927.App;

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
        sendPacket(new SendReceptionPacket());
        receiveInit(PacketType.SETLEVEL);
    }

    @Override
    public void run() {

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
                receivedPacket = new ReceivedVersionPacket(parameters);
                break;
            case PacketType.QUIT:
                receivedPacket = new ReceivedQuitPacket();
                break;
            case PacketType.NEWJOINER:
                receivedPacket = new ReceivedNewJoinerPacket();
                break;
            case PacketType.POSITION:
                receivedPacket = new ReceivedPositionPacket();
                break;
            case PacketType.REMOVEPLAYER:
                receivedPacket = new ReceivedRemovePlayerPacket();
                break;
            case PacketType.NEWENTITY:
                receivedPacket = new ReceivedNewEntityPacket();
                break;
            case PacketType.SETLEVEL:
                receivedPacket = new ReceivedSetLevelPacket(parameters, app);
                break;
            case PacketType.REMOVELEVEL:
                receivedPacket = new ReceivedRemoveLevelPacket();
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

    private void updatePlayerPosition(String line) {
        try {
            app.getPlaying().getPlayers().get(Integer.parseInt(line.split(" ")[1])).setPosition(Float.parseFloat(line.split(" ")[2])*App.SCALE, Float.parseFloat(line.split(" ")[3])*App.SCALE);
            app.getPlaying().getPlayers().get(Integer.parseInt(line.split(" ")[1])).setPlayerAction(Integer.parseInt(line.split(" ")[4]));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error while receiving player position", e);
        }
    }

    public void sendUpdates() {
        // sendPacket("PLAYER "+app.getPlaying().getPlayer().getData());
    }

    public void close() {
        // sendPacket("QUIT");
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

    private void sendPacket(SendPacket packet) {
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
	 * return a byte array from the int
	 * @param value the value we wants to convert into bytes
	 * @param bytesSize the number of bytes we wants [1-4]
	 * @return the array
	 */
	private int bytesToInt(byte[] array) {
		int value = 0;
		switch (array.length) {
			case 4:
                value = (((int) array[0]) << 24) + (((int) array[1]) << 16) + (((int) array[2]) << 8) + (((int) array[3]) << 0);
				break;
			case 3:
                value = (((int) array[0]) << 16) + (((int) array[1]) << 8) + (((int) array[2]) << 0);
				break;
			case 2:
                value = (((int) array[0]) << 8) + (((int) array[1]) << 0);
				break;
			case 1:
                value = (int) array[0];
				break;
			default:
				break;
		}
		return value;
	}
}
