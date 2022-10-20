package com.jbmo60927.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.logger.MyLogger;


public class UserRunnable implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(UserRunnable.class.getName()); //logger for this class
    private final Socket socketOfServer; //socket of the client
    private final BufferedReader is; //input stream
    private final BufferedWriter os; //output stream

    /**
     * thread to communicate with a unique client
     * @param socketOfServer socket of the client
     * @param clientNumber id of the client
     * @param app link to the data
     * @throws IOException exception that could occure when the communication is closed badly 
     */
    public UserRunnable(final Socket socketOfServer) throws IOException {
        LOGGER.setLevel(Level.INFO);
        this.socketOfServer = socketOfServer;

        //open input and output streams
        is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
    }

    /**
     * thread to receive and send data to the client
     */
    public void run() {
        synchronized(this) {
            try {
                LOGGER.log(Level.INFO, "running");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          }
    }

    public void shutdown() {
        try {
            is.close();
            os.close();
            LOGGER.log(Level.INFO, "shutdown");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
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

    public static void main(String[] args) throws InterruptedException, IOException {
        MyLogger.setup(); //setup the logger for the app
        try (ServerSocket server = new ServerSocket(7777)) {
            LOGGER.log(Level.INFO, String.format("ip:%s", getIpV4Address()));
            final UserRunnable test = new UserRunnable(server.accept());
            Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
                test.shutdown();
            }});
            Thread t = new Thread(test);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
    
}