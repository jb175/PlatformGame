package com.jbmo60927.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.jbmo60927.App;
import com.jbmo60927.entities.Player;

public class ServiceThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ServiceThread.class.getName()); //logger for this class
    private final int clientNumber; //id of the client
    private final Socket socketOfServer; //socket of the client
    private final App app; //link to the data
    private final BufferedReader is; //input stream
    private final BufferedWriter os; //output stream

    /**
     * thread to communicate with a unique client
     * @param socketOfServer socket of the client
     * @param clientNumber id of the client
     * @param app link to the data
     * @throws IOException exception that could occure when the communication is closed badly 
     */
    public ServiceThread(final Socket socketOfServer, final int clientNumber, final App app) throws IOException {
        LOGGER.setLevel(Level.INFO);
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;
        this.app = app;

        //open input and output streams
        is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

        
        //receive data from client
        String line;
        try {
            do {
                line = is.readLine();
            } while (line.split(" ")[0].compareTo("INITPLAYER") != 0);
            final Float xPos = Float.parseFloat(line.split(" ")[1]);
            final Float yPos = Float.parseFloat(line.split(" ")[2]);
            final String name = line.split(" ")[3];
    
            //add player
            app.getPlayers().put(this, new Player(xPos, yPos, name));
    
            //send client parameters to all other clients
            broadcast(String.format("NEWPLAYER %d %s", clientNumber, clientNumber+line.replace("INITPLAYER ", "")));
    
            for (final ServiceThread thread : app.getPlayers().keySet()) {
                if(thread != this) {
                    os.write(String.format("NEWPLAYER %d %s %s %s", thread.getClientNumber(), app.getPlayers().get(thread).getX(), app.getPlayers().get(thread).getY(), app.getPlayers().get(thread).getName()));
                    os.newLine();
                    os.flush();
                }
            }
    
            //send data to client
            os.write("INITDATA ");
            os.newLine();
            os.flush();
    
            //log connection
            LOGGER.log(Level.INFO, () -> String.format("New connection with client# %d at %s named %s", this.clientNumber, this.socketOfServer.getInetAddress().toString().replace("/", ""), name));
        
        //in case the client is not a game client (packet sends are not required)
        } catch (Exception e) {
            String ip = this.socketOfServer.getInetAddress().toString().replace("/", "");
            String country = "";
            String city = "";
            try {
                URL url = new URL("https://www.iplocation.net/ip-lookup?query="+ip+"&submit=IP%20Lookup");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Now it's "open", we can set the request method, headers etc.
                connection.setRequestProperty("accept", "application/json");

                // This line makes the request
                InputStream responseStream = connection.getInputStream();
                Document html = Jsoup.parse(new String(responseStream.readAllBytes()));
                country = html.body().getElementsByClass("table_dark_green").first().child(1).child(0).child(1).text();
                city = html.body().getElementsByClass("table_dark_green").first().child(1).child(0).child(3).text();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            LOGGER.log(Level.SEVERE, String.format("connection cannot be initalized (ip:%s country:%s city:%s)", ip, country, city), e);
            this.interrupt();
        }
    }

    /**
     * thread to receive and send data to the client
     */
    @Override
    public void run() {
        try {
            while (true) {

                //read data to the server (sent from client).
                final String line = is.readLine();

                //log the paquet
                LOGGER.log(Level.FINEST, () -> String.format("paquet received: %s", line));

                //new position for a player
                if (line.split(" ")[0].compareTo("PLAYER") == 0) {
                    app.getPlayers().get(this).setX(Float.parseFloat(line.split(" ")[1]));
                    app.getPlayers().get(this).setY(Float.parseFloat(line.split(" ")[2]));
                    app.getPlayers().get(this).setPlayerAction(Integer.parseInt(line.split(" ")[3]));
                    broadcast(String.format("PLAYER %d %s", clientNumber, line.replace("PLAYER ", "")));

                //if users send QUIT (To end conversation).
                } else if (line.equals("QUIT")) {
                    app.getPlayers().remove(this);
                    broadcast(String.format("REMOVEPLAYER %d", clientNumber));

                    is.close();
                    os.close();
                    LOGGER.log(Level.INFO, () -> String.format("Connection stop with client# %d at %s", this.clientNumber, this.socketOfServer.getInetAddress().toString().replace("/", "")));

                    break;
                
                //else display command
                } else {
                    LOGGER.log(Level.INFO, line);
                }
            }

        //error
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "error during the service thread", e);
        }
    }

    /**
     * allow to write output from outside of the thread
     * @return the buffer to write
     */
    public BufferedWriter getOs() {
        return os;
    }

    /**
     * get the id of the client
     * @return the id
     */
    public int getClientNumber() {
        return clientNumber;
    }

    /**
     * broadcast a paquet to every client
     * @param trame the paquet to broadcast
     * @throws IOException exception that could occure if something is wrong with the connection
     */
    private void broadcast(final String trame) throws IOException {
        //send data to every other clients
        for (final ServiceThread thread : app.getPlayers().keySet()) {
            if(thread != this) {
                thread.getOs().write(trame);
                thread.getOs().newLine();
                thread.getOs().flush();
            }
        }
    }
}