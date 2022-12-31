package com.jbmo60927.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.command.Command;

public class ServiceThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ServiceThread.class.getName());

    private Socket socketOfClient;
    private App app;

    private final ReceivePacketThread receveivePacket;
    private final SendPacketThread sendPacket;
    
    private final BufferedReader kb; //keyboard input stream
    
    public ServiceThread(Socket socketOfClient, App app) throws IOException {
        LOGGER.setLevel(Level.FINEST);

        this.app = app;
        this.socketOfClient = socketOfClient;

        this.receveivePacket = new ReceivePacketThread(this.socketOfClient, this.app);
        this.sendPacket = new SendPacketThread(this.socketOfClient, this.app);

        this.receveivePacket.start();
        
        kb = new BufferedReader(new InputStreamReader(System.in));
    }

    public ReceivePacketThread getReceveivePacket() {
        return receveivePacket;
    }

    public SendPacketThread getSendPacket() {
        return sendPacket;
    }

    @Override
    public void run() {
        while (true) {
            readCommand();
        }
    }

    private void readCommand() {
        try {
            String command = kb.readLine();
            listenCommand(command);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "impossible to read into keyboard input", e);
        }
    }

    private void listenCommand(String command) {
        if (!"".equals(command)) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, String.format("new command: %s", command));
            }
            try {
                String returnString = Command.readCommand(command).execute(app);
                if (LOGGER.isLoggable(Level.INFO) && !returnString.equals(""))
                    LOGGER.log(Level.INFO, returnString);
            } catch (ClassNotFoundException e) {
                if (LOGGER.isLoggable(Level.INFO))
                    LOGGER.log(Level.INFO, () -> "the command doesn't exist");
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                LOGGER.log(Level.SEVERE, "the command is not understand", e);
            }
        }
    }

    @Override
    public void interrupt() {
        try {
            socketOfClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.receveivePacket.interrupt();
        super.interrupt();
    }
}
