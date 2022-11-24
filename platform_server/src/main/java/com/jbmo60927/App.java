package com.jbmo60927;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.entities.Player;
import com.jbmo60927.levels.LevelHandler;
import com.jbmo60927.logger.MyLogger;
import com.jbmo60927.thread.AcceptUserThread;
import com.jbmo60927.thread.ServiceThread;
import com.jbmo60927.utilz.PropertyFile;

/**
 * Game app central part
 */
public final class App {

    private final int localPort; //default port 
    private final String version; //default version value
    private Thread acceptUserThread; //thread to accept new client
    private final HashMap<ServiceThread,Player> players = new HashMap<>(); //map of all player and their thread
    private final PropertyFile propertyFile; //property file
    private final String propertyFileName = "com/jbmo60927/properties/server.xml"; //path for the property file
    private final BufferedReader ks; //keyboard input stream

    public static final int TILE_IN_WIDTH = 26;
    public static final int TILE_IN_HEIGHT = 14;
    private LevelHandler LevelHandler;

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    
    /**
     * initiate the server.
     * @throws IOException exception that could occure
     */
    private App() throws IOException {
        //setup log level
        LOGGER.setLevel(Level.CONFIG);

        //read configuration file
        propertyFile = new PropertyFile(this.propertyFileName);

        //read properties from the file
        this.version = readStringProperty(this.propertyFile, "version");
        this.localPort = readIntProperty(this.propertyFile, "port");

        //read levelData
        LevelHandler = new LevelHandler();

        //open the selected port
        ServerSocket listener = null;
        try {
            LOGGER.log(Level.FINE, "local port correctly open");
            listener = new ServerSocket(localPort);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Cannot open server", e);
            System.exit(1);
        }

        try {
            //allow client to connect to the port
            acceptUserThread = new AcceptUserThread(listener, this);
            acceptUserThread.start();

            ks = new BufferedReader(new InputStreamReader(System.in));
            LOGGER.log(Level.INFO, () -> String.format("Server (ip:%s port:%s) is waiting to accept user...", getIpV4Address(), Integer.toString(localPort)));

            String str;
            while ((str = ks.readLine()) != null) {
                LOGGER.log(Level.INFO, str);
            }
        
        //stop the server
        } finally {
            //save properties
            System.exit(0);
        }
    }

    /**
     * read a string property
     * @param propertyFile the file from where we read the property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public String readStringProperty(final PropertyFile propertyFile, final String name) {
        if (propertyFile.getProperties().getProperty(name) != null) {
            LOGGER.log(Level.CONFIG, () -> String.format("%s sucessfully read", name));
            return propertyFile.getProperties().getProperty(name);
        } else {
            LOGGER.log(Level.SEVERE, () -> String.format("%s cannot be read on the server.xml file", name));
            System.exit(1);
        }
        return "";
    }

    /**
     * read an integer property
     * @param propertyFile the file from where we read the property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public int readIntProperty(final PropertyFile propertyFile, final String name) {
        if (propertyFile.getProperties().getProperty(name) != null) {
            LOGGER.log(Level.CONFIG, () -> String.format("%s sucessfully read", name));
            try {
                return Integer.parseInt(propertyFile.getProperties().getProperty(name));
            } catch (final Exception e) {
                LOGGER.log(Level.SEVERE, "cannot parse string property into an integer", e);
            }
        } else {
            LOGGER.log(Level.SEVERE, () -> String.format("%s cannot be read on the server.xml file", name));
            System.exit(1);
        }
        return 0;
    }
    
    /**
     * get the ipv4 adress of the server
     * @return the ipv4 adress of the server
     */
    public static String getIpV4Address() {
        List<String> ip = new ArrayList<>();
        final List<String> ipRefined = new ArrayList<>();
        try {
            ip = getIpAddresses();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "cant find the ip address", e);
        }
        for(int x = 0; x < ip.size(); x++){
            if(ip.get(x).contains("%")){
                try {
                    if (ip.get(x + 1).contains(".")) {
                        ipRefined.add(ip.get(x + 1));
                    }
                } catch (final IndexOutOfBoundsException e) {
                    continue;
                }
                LOGGER.log(Level.FINE, "ip found");
            }
        }
        return ipRefined.get(0);
    }

    /**
     * get the ipv4 adress of the server
     * @return the ipv4 adress of the server
     * @throws SocketException exception with the socket
     */
    public static List<String> getIpAddresses() throws SocketException {
        final List<String> ip = new ArrayList<>();
        final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            final NetworkInterface iface = interfaces.nextElement();
            if (iface.isLoopback() || !iface.isUp())
                continue;
            final Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                ip.add(addresses.nextElement().getHostAddress());
            }
        }
        return ip;
    }

    /**
     * link player and thread together.
     * @return a map of all the player and their associate thread
     */
    public Map<ServiceThread, Player> getPlayers() {
        return players;
    }

    public String getVersion() {
        return this.version;
    }

    public LevelHandler getLevelHandler() {
        return this.LevelHandler;
    }

    /**
     * Run the server for the game Platform.
     * @param args The arguments of the program.
     * @throws IOException When the logger can't be created.
     */
    public static void main(final String[] args) throws IOException {
        MyLogger.setup(); //setup the logger for the app
        new App(); //begin the app
    }
}
