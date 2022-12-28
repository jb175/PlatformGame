package com.jbmo60927.thread;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.communication.packets.Packet;

public class ReceivePacketThread extends Thread {

    private Queue<Packet> queue = new LinkedList<>();
    private final InputStream is; //input stream
    private static final Logger LOGGER = Logger.getLogger(ReceivePacketThread.class.getName());

    public ReceivePacketThread(Socket socketOfServer) throws IOException {
        is = socketOfServer.getInputStream();
        LOGGER.setLevel(Level.INFO);
    }

    @Override
    public void run() {
        while (true) {
            receiveFirstPacket();
        }
    }

    public void sendPacket(Packet packet) {
        queue.add(packet);
    }

    private String receiveFirstPacket() {
        Packet packet = null;
        try {
            packet = Packet.readPacket(is);
        } catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "impossible to read into input stream", e);
        }

        //return the message
        if (packet != null) {
            packet.execute();
            return packet.getMessage();
        }
        return "";
    }

    private String receiveSpecificPacket(int packetTypeExpected) {
        Packet packet = null;
        try {
            do {
                packet = Packet.readPacket(is);
            } while (packet.getPacketType() != packetTypeExpected);
        } catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "impossible to read into input stream", e);
        }

        //return the message
        if (packet != null) {
            packet.execute();
            return packet.getMessage();
        }
        return "";
    }
}