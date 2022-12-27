package com.jbmo60927.thread;

import static com.jbmo60927.utilz.HelpsMethods.bytesToInt;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.communication.packets.Packet;
import com.jbmo60927.communication.parameters.Parameter;

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

    private static int readPacketType(InputStream is) throws IOException {
        return bytesToInt(is.readNBytes(Packet.PACKET_TYPE_BYTES));
    }

    private static int readPacketParametersSize(InputStream is) throws IOException {
        return bytesToInt(is.readNBytes(Packet.PACKET_PARAMETER_NUMBER_BYTES));
    }

    private static Parameter[] readPacketParameters(InputStream is, int parameterNumber) throws IOException {
        Parameter[] parameters = new Parameter[parameterNumber];
        int parameterType;
        int parameterSize;
        for (int i = 0; i < parameterNumber; i++) {
            parameterType = bytesToInt(is.readNBytes(Parameter.PARAMETER_TYPE_BYTES));
            parameterSize = bytesToInt(is.readNBytes(Parameter.PARAMETER_SIZE_BYTES));
            parameters[i] = new Parameter(parameterType, is.readNBytes(parameterSize));
        }

        return parameters;
    }

    private static Packet receive(InputStream is) throws IOException {
        int packetType = readPacketType(is);
        int packetParameterNumber = readPacketParametersSize(is);
        Parameter[] parameters = readPacketParameters(is, packetParameterNumber);
        if (LOGGER.isLoggable(Level.FINEST)) {
            String log = "packet received: " + packetType + "with parameters:\n";
            for (final Parameter parameter : parameters) {
                log += parameter.getParameterType() + ": " + parameter.getValue();
            }
            LOGGER.log(Level.FINEST, log);
        }
        return Packet.createPacket(packetType, true, parameters);
    }

    private String receiveFirstPacket() {
        Packet packet = null;
        try {
            packet = receive(is);
        } catch (IOException e) {
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
                packet = receive(is);
            } while (packet.getPacketType() != packetTypeExpected);
        } catch (IOException e) {
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