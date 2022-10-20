package com.jbmo60927.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.logger.MyLogger;


public class GameLinkRunnable implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(GameLinkRunnable.class.getName()); //logger for this class
    private final Socket socketOfClient; //socket of the client
    private final BufferedReader is; //input stream
    private final BufferedWriter os; //output stream

    /**
     * thread to communicate with a unique client
     * @param socketOfClient socket of the client
     * @param clientNumber id of the client
     * @param app link to the data
     * @throws IOException exception that could occure when the communication is closed badly 
     */
    public GameLinkRunnable(final Socket socketOfClient) throws IOException {
        LOGGER.setLevel(Level.INFO);
        this.socketOfClient = socketOfClient;

        //open input and output streams
        is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
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
        System.out.println("shutdown");
        try {
            is.close();
            System.out.println("shutdown");
            os.close();
            System.out.println("shutdown");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("shutdown");
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        MyLogger.setup(); //setup the logger for the app
        try {
            String ip = "172.28.29.115";
            Socket socketOfClient = new Socket(ip, 7777);
            final GameLinkRunnable test = new GameLinkRunnable(socketOfClient);
            Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
                test.shutdown();
            }});
            Thread t = new Thread(test);
            t.start();


            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();;
            while (!"quit".equals(line)) {
                line = sc.nextLine();
            }
            sc.close();
            t.interrupt();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }
    
}