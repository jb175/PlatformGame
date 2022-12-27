package com.jbmo60927.thread;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.communication.packets.Packet;
import com.jbmo60927.communication.parameters.Parameter;

public class SendPacketThread extends Thread {

    private final Queue<Packet> queue = new LinkedList<>();
    private final OutputStream os; //output stream
    private static final Logger LOGGER = Logger.getLogger(SendPacketThread.class.getName());

    public SendPacketThread(final Socket socketOfServer) throws IOException {
        os = socketOfServer.getOutputStream();
        LOGGER.setLevel(Level.FINEST);
    }

    @Override
    public void run() {
        while (true) {
            if (!queue.isEmpty())
                send(queue.remove(), os);
        }
    }

    public void sendPacket(final int packetType, final Parameter[] parameters) {
        final Packet packet = Packet.createPacket(packetType, false, parameters);
		sendPacket(packet);
    }

    public void sendPacket(final Packet packet) {
        queue.add(packet);
    }

    private static void send(final Packet packet, final OutputStream os) {
        try {
            os.write(Packet.getCompactPacket(packet));
            os.flush();
            if (LOGGER.isLoggable(Level.FINEST)) {
                String log = "packet send: " + packet.getPacketType() + "with parameters:\n";
                for (final Parameter parameter : packet.getParameters()) {
                    log += parameter.getParameterType() + ": " + parameter.getValue();
                }
                LOGGER.log(Level.FINEST, log);
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "error while sending packet", e);
        }
    }
}