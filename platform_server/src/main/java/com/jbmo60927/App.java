package com.jbmo60927;

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
import com.jbmo60927.logger.MyLogger;
import com.jbmo60927.thread.AcceptUserThread;
import com.jbmo60927.thread.ServiceThread;
import com.jbmo60927.utilz.PropertyFile;

/**
 * Hello world!
 */
public final class App {

    static Properties serverProperties = new Properties();
    private int localPort=7777;
    private String version="1.0";
    private Thread acceptUserThread;
    private HashMap<ServiceThread,Player> players = new HashMap<>();
    private PropertyFile propertyFile;

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    
    private App() throws IOException {
        //setup log level
        LOGGER.setLevel(Level.INFO);
        LOGGER.log(Level.INFO, "test");

        //read configuration file
        propertyFile = new PropertyFile("com/jbmo60927/utilz/server.xml");

        this.localPort = readIntProperty("port");
        this.version = readStringProperty("version");

        //open the selected port 
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(localPort);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Cannot open server", e);
            System.exit(1);
        }

        try {
            //allow client to connect to the port
            acceptUserThread = new AcceptUserThread(listener, this);
            acceptUserThread.start();
            LOGGER.log(Level.INFO, () -> String.format("Server (ip:%s port:%s) is waiting to accept user...", getIpAddress().get(0), Integer.toString(localPort)));


            while (true) {
                //wait for the server to be stopped
            }
        } finally {
            System.exit(0);
        }
    }

    public String readStringProperty(String name) {
        if (this.propertyFile.getProperties().getProperty(name) != null) {
            LOGGER.log(Level.FINE, "{} sucessfully read", name);
            return this.propertyFile.getProperties().getProperty(name);
        } else {
            LOGGER.log(Level.SEVERE, "{} cannot be read on the server.xml file", name);
            System.exit(1);
        }
        return "";
    }

    public int readIntProperty(String name) {
        if (this.propertyFile.getProperties().getProperty(name) != null) {
            LOGGER.log(Level.FINE, "{} sucessfully read", name);
            return Integer.parseInt(this.propertyFile.getProperties().getProperty(name));
        } else {
            LOGGER.log(Level.SEVERE, "{} cannot be read on the server.xml file", name);
            System.exit(1);
        }
        return 0;
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

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     * @throws IOException when the logger can't be created
     */
    public static void main(final String[] args) throws IOException {
        MyLogger.setup();

        new App();
    }
}
