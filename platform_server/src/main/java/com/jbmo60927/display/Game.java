package com.jbmo60927.display;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.entities.Player;

public class Game {

    static Properties serverProperties = new Properties();
    private static int localPort;
    private Thread acceptUserThread;
    private HashMap<ServiceThread,Player> players = new HashMap<>();

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());


    /**
     * Read configuration file or generate it if it is the first time the server run
     */
    static {
        
        for (int index = 0; index < 3; index++) { //we try to read/generate the configuration file up to 3 times
            try {
                serverProperties.load(new FileInputStream("./server.properties"));
                LOGGER.log(Level.INFO, "Server configuration file sucessfully read");
                break;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Cannot read the server configuration file", e);
                try {
                    FileWriter myWriter = new FileWriter("server.properties");
                    myWriter.write("#default port for the app\n");
                    myWriter.write("com.jbmo60927.platform_server.port=7777\n");
                    myWriter.close();
    
                    LOGGER.log(Level.INFO, "Server configuration file has been generated");
                } catch (IOException e2) {
                    LOGGER.log(Level.SEVERE, "The server configuration file cannot be generated", e2);
                }
            }
        }
        try { // read the properties
            localPort = Integer.parseInt((String) serverProperties.get("com.jbmo60927.platform_server.port"));
            LOGGER.log(Level.INFO, "default port successfully read");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Default port cannot be read on server.configuration file", e);
            localPort = 7777;
            serverProperties.setProperty("com.jbmo60927.platform_server.port", "7777");
            try {
                serverProperties.store(new FileOutputStream("./server.properties"), "last port cannot be used");
                LOGGER.log(Level.INFO, "Default port has been reset to 7777");
            } catch (Exception e2) {
                LOGGER.log(Level.SEVERE, "Default port value cannot be reset on the file", e2);
            }
        }
    }

    public Game() throws IOException {
        //setup log level
        LOGGER.setLevel(Level.INFO);

        //open the selected port 
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(localPort);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Cannot open server", e);
            System.exit(1);
        }

        //allow client to 
        acceptUserThread = new AcceptUserThread(listener, this);
        LOGGER.log(Level.INFO, "Server (ip:{0} port:{1}) is waiting to accept user...", new Object[] {getIpAddress().get(0), localPort});

        try {

            //receive connections
            acceptUserThread.start();
            while (true) {
                //wait for the server to be stopped
            }
        } finally {
            System.exit(0);
        }
    }
    
    public static List<String> getIpAddress() {
        List<String> ip = new ArrayList<>();
        List<String> ipRefined = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    ip.add(addresses.nextElement().getHostAddress());
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        for(int x = 0; x < ip.size(); x++){
            if(ip.get(x).contains("%")){
                try {
                    if (ip.get(x + 1).contains(".")) {
                        ipRefined.add(ip.get(x + 1));
                    }
                } catch (IndexOutOfBoundsException ae) {
                }
            }
        }
        return ipRefined;
    }

    public Map<ServiceThread, Player> getPlayers() {
        return players;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}