package com.jbmo60927.display;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.jbmo60927.entities.Player;

public class Game {

    static Properties serverProperties = new Properties();
    private static int localPort;
    private Thread acceptUserThread;
    private HashMap<ServiceThread,Player> players = new HashMap<>();

    private static final LogManager LOGMANAGER = LogManager.getLogManager();

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private final Scanner sc = new Scanner(System.in);

    /**
     * Read configuration file or generate it if it is the first time the server run
     */
    static {
        for (int index = 0; index < 3; index++) { //we try to read/generate the configuration file up to 3 times
            try {
                LOGMANAGER.readConfiguration(new FileInputStream("./logger.properties"));
                LOGGER.log(Level.INFO, "Logger Configuration file sucessfully read");
                break;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Cannot read the logger configuration file", e);
                try {
                    FileWriter myWriter = new FileWriter("logger.properties");
                    myWriter.write("# On log sur la console et dans un fichier.\n");
                    myWriter.write("handlers=java.util.logging.ConsoleHandler, java.util.logging.FileHandler\n\n");
                    
                    myWriter.write("# On peut configurer le ConsoleHandler, mais ici j'utilise sa configuration par défaut.\n");
                    myWriter.write("# java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter (c'est pas défaut)\n\n");
                    
                    myWriter.write("# On configure notre FileHandler (il utilise lui aussi un SimpleFormatter).\n");
                    myWriter.write("java.util.logging.FileHandler.formatter=java.util.logging.SimpleFormatter\n");
                    myWriter.write("java.util.logging.FileHandler.pattern=app-%u-%g.log\n\n");
                    
                    myWriter.write("# On change le format des logs pour notre SimpleFormatter.\n");
                    myWriter.write("java.util.logging.SimpleFormatter.format=[%1$tF %1$tT][%2$s] %4$s: %5$s %n\n\n");
                    
                    myWriter.write("# Rappels sur les niveaux : OFF / SEVERE / WARNING / INFO / CONFIG / FINE / FINER / FINEST / ALL\n");
                    myWriter.write("# On limite tous les logs des autres composants (des autres packages) à l'affichage des erreurs.\n");
                    myWriter.write("# .level=SEVERE\n\n");
                    
                    myWriter.write("# On active les logs du package fr.koor.samples.jul sur INFO (et donc WARNING et SEVERE).\n");
                    myWriter.write("com.jbmo60927.display.level=INFO\n");
                    myWriter.close();
    
                    LOGGER.log(Level.INFO, "Logger configuration file has been generated");
                } catch (IOException e2) {
                    LOGGER.log(Level.SEVERE, "Logger configuration file cannot be generated", e2);
                }
            }
        }
        
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
        ServerSocket listener = null;
        LOGGER.log(Level.INFO, "Server (ip:{0} port:{1}) is waiting to accept user...", new Object[] {InetAddress.getLocalHost().getHostAddress(), localPort});

        //open the server to get connections
        try {
            listener = new ServerSocket(localPort);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Cannot open server", e);
            System.exit(1);
        }

        acceptUserThread = new AcceptUserThread(listener, this);

        try {

            //receive connections
            acceptUserThread.start();

            //commands
            while (true) {
                String command = sc.nextLine();
                
                if(command.equals("stop")) {
                    LOGGER.log(Level.INFO, "Server is closing...");
                    acceptUserThread.interrupt();
                    LOGGER.log(Level.INFO, "Users cannot join anymore");
                    listener.close();
                    LOGGER.log(Level.INFO, "Users are kicked from the server");

                    LOGGER.log(Level.INFO, "server closed");
                    break;
                } else if (command.equals("player")) {
                    LOGGER.log(Level.INFO, "connected player:{0}", players);
                } else {
                    LOGGER.log(Level.INFO, "{0}", command);
                }
            }
        } finally {
            System.exit(0);
        }
    }

    public Map<ServiceThread, Player> getPlayers() {
        return players;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}