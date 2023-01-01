package com.jbmo60927.thread;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.packets.Packet;

public class SendPacketThread{

    //output stream to write data to the client/server
    private final OutputStream os;

    //the app to 
    private final App app;

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(SendPacketThread.class.getName());

    /**
     * this class is used to send packets to the client/server
     * @param socketOfServer the socket where we should write data
     * @throws IOException exeption that could occure when the socket is closed
     */
    public SendPacketThread(final Socket socketOfServer, final App app) throws IOException {
        this.os = socketOfServer.getOutputStream();
        LOGGER.setLevel(Level.FINE);
        this.app = app;
    }

    /**
     * send packet public method
     * @param packet the packet to send
     */
    public void sendPacket(final Packet packet) {
        //we call the static method
        send(packet, this.os);
    }

    /**
     * send packet static method
     * @param packet the packet to send
     * @param os the output stream used to write data on
     */
    private static void send(final Packet packet, final OutputStream os) {
        try {

            //if we are autorized we log a description of the packet to send
            StringBuilder log = new StringBuilder();
            log.append(String.format("SEND new PACKET%ntype:%s, parameter number:%d parameters:", packet.getPacketType().getTypeName(), packet.getParameters().length));
            int parameterNumber = 0;
            for (final Parameter parameter : packet.getParameters())
                log.append(String.format("%n\t%d: type:%s, size:%d, value:%s", parameterNumber++, parameter.getParameterType().getTypeName(), parameter.getValue().length, bytesToString(parameter.getValue())));
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, log.toString());

            //we get the packet ready to be send (raw-packet)
            final byte[] rawPacket = Packet.getCompactPacket(packet);

            //if we are autorized we log the raw-packet
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, () -> String.format("SEND new RAW-PACKET: %s", bytesToString(rawPacket)));
            
            //we write the packet into the output stream
            os.write(rawPacket);

            //we send the data
            os.flush();

        } catch (final IOException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | ClassNotFoundException e) {

            //if an exception is raised we log it
            LOGGER.log(Level.SEVERE, "error while sending packet", e);
        }
    }
}