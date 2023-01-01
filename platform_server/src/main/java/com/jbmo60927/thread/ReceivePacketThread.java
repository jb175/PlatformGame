package com.jbmo60927.thread;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.packets.Packet;

public class ReceivePacketThread extends Thread {

    //input stream to read data from the client/server
    private final InputStream is;

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(ReceivePacketThread.class.getName());

    //access to the app to be able to close the connection and transfer game data
    private final App app;

    //access this thread
    ServiceThread serviceThread;

    /**
     * this class is used to receive packets from the client/server
     * @param socketOfServer the socket where we should write data
     * @param serviceThread the service thread that strat this thread
     * @throws IOException exeption that could occure when the socket is closed
     */
    public ReceivePacketThread(final Socket socketOfServer, final App app, final ServiceThread serviceThread) throws IOException {
        this.is = socketOfServer.getInputStream();
        LOGGER.setLevel(Level.FINE);
        this.app = app;
        this.serviceThread = serviceThread;
    }

    /**
     * the thread should listen the port for new packet all the time
     */
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            receive();
        }
    }

    /**
     * when a packet is detected it will read it entirely and then try to listen it to create the corresponding packet
     */
    private void receive() {
        //the packet is null at start and if an error occure before the value is change it will stay a null packet
        Packet packet = null;
        try {

            //we receive the raw packet
            final byte[] rawPacket = Packet.readRawPacket(this.is);

            //if we are autorized we log the raw-packet
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, () -> String.format("RECEIVE new RAW-PACKET: %s", bytesToString(rawPacket)));

            
            //we crete the corresponding packet
            packet = Packet.readPacket(rawPacket, app, serviceThread.getPlayerListNumber());

        } catch (final SocketException e) {

            //the socket exeption occure when the port is closed on the other side so the thread can't read any more data
            this.serviceThread.interrupt();
            //we log a message to explain
            LOGGER.log(Level.SEVERE, "socket closed");

        } catch (IOException | IllegalArgumentException | SecurityException e) {

            //if an other error is raised we show this general error
            LOGGER.log(Level.SEVERE, "impossible to read into input stream", e);
        }

        //if the packet is not null we log the packet and execute it
        if (packet != null) {

            //if we are autorized we log a description of the packet to send
            String log = String.format("RECEIVE new PACKET%ntype:%s, parameter number:%d parameters:", packet.getPacketType().getTypeName(), packet.getParameters().length);
            int parameterNumber = 0;
            for (final Parameter parameter : packet.getParameters()) {
                log += String.format("%n\t%d: type:%s, size:%d, value:%s", parameterNumber++, parameter.getParameterType().getTypeName(), parameter.getValue().length, bytesToString(parameter.getValue()));
            }
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, log);


            //we execute the packet
            packet.execute();
        } else {

            //we close the connection
            this.serviceThread.interrupt();
            //we log a message to explain
            LOGGER.log(Level.SEVERE, "socket closed");
        }
    }
}