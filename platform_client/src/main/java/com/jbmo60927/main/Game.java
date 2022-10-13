package com.jbmo60927.main;

import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import com.jbmo60927.gamestates.Connect;
import com.jbmo60927.gamestates.GameStates;
import com.jbmo60927.gamestates.Menu;
import com.jbmo60927.gamestates.Playing;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Game implements Runnable {

    //object to create the panel for the window
    private GamePanel gamePanel;

    //thread running continuously to display the game
    private Thread gameThread;

    //default value for the game refreshement
    private static final int UPSSET = 200;
    //default value for the game display update
    private static int fpsSet = 120;
    //default value for the size of the game
    public static float scale = 1.5f;

    private Playing playing;
    private Menu menu;
    private Connect connect;

    public static final int TILE_DEFAULT_SIZE = 32;
    public static final int TILE_IN_WIDTH = 26;
    public static final int TILE_IN_HEIGHT = 14;
    public static int TILE_SIZE = (int)(TILE_DEFAULT_SIZE * scale);
    public static int GAME_WIDTH = TILE_SIZE * TILE_IN_WIDTH;
    public static int GAME_HEIGHT = TILE_SIZE * TILE_IN_HEIGHT;

    private static final LogManager LOGMANAGER = LogManager.getLogManager();
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());
    
    static Properties clientProperties = new Properties();

    public static final String LOGGER_CONFIG_PATH = "logger.properties";
    public static final String CLIENT_CONFIG_PATH = "./client.properties";


    protected static final Object[][] CLIENT_CONFIG_LIST = {{"upsSet", 200}, {"fpsSet", 120}, {"scale", 1.5f}};
    public static final String CONFIG_START = "com.jbmo60927.main.Game.";
	public static final Object[] CLIENT_CONFIG_PROPERTY = new Object[CLIENT_CONFIG_LIST.length];

    public static final int UPS_DEFAULT_VALUE = 200;
    public static final int FPS_DEFAULT_VALUE = 120;
    public static final float SCALE_DEFAULT_VALUE = 1.5f;

    static {
        for (int index = 0; index < 3; index++) { //we try to read/generate the configuration file up to 3 times
            try {
                LOGMANAGER.readConfiguration(new FileInputStream("./"+LOGGER_CONFIG_PATH));
                LOGGER.log(Level.INFO, "Logger Configuration file sucessfully read");
                break;
            } catch (SecurityException | IOException e) {
                LOGGER.log(Level.SEVERE, "Cannot read the logger configuration file", e);
                try {
                    FileWriter myWriter = new FileWriter(LOGGER_CONFIG_PATH);
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
                clientProperties.load(new FileInputStream(CLIENT_CONFIG_PATH));
                LOGGER.log(Level.INFO, "Client configuration file sucessfully read");
                break;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Cannot read the client configuration file", e);
                try {
                    FileWriter myWriter = new FileWriter(CLIENT_CONFIG_PATH);
                    for (int index2 = 0; index2 < CLIENT_CONFIG_LIST.length; index2++) {
                        myWriter.write(CONFIG_START+CLIENT_CONFIG_LIST[index2][0].toString()+"="+CLIENT_CONFIG_LIST[index2][1].toString()+"\n");
                    }
                    myWriter.close();
    
                    LOGGER.log(Level.INFO, "Client configuration file has been generated");
                } catch (IOException e2) {
                    LOGGER.log(Level.SEVERE, "The client configuration file cannot be generated", e2);
                }
            }
        }

        
    }

    public Game() {
        readConfigFiles();
        initClasses("test");

        gamePanel = new GamePanel(this);
        new GameWindow(gamePanel, this);

        //set focus on pannel
        while (!gamePanel.isFocusOwner()) {
            gamePanel.requestFocus();
        }

        startGameLoop();
    }

    private void readConfigFiles() {
        // read the properties
        for (int i = 0; i < CLIENT_CONFIG_LIST.length; i++) {
            //if the property is correctly assign in the config file
            try {
                System.out.println(this.getClass().getDeclaredField(CLIENT_CONFIG_LIST[i][0].toString()).get(this));
                this.getClass().getDeclaredField(CLIENT_CONFIG_LIST[i][0].toString()).set(this, CLIENT_CONFIG_LIST[i][1]);
                System.out.println(this.getClass().getDeclaredField(CLIENT_CONFIG_LIST[i][0].toString()).get(this));

                System.out.println(CLIENT_CONFIG_LIST[i][0].toString()+CLIENT_CONFIG_LIST[i][1].toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LOGGER.log(Level.INFO, "{0} ({1}) successfully read on client.properties file", new Object[] {CLIENT_CONFIG_LIST[i][0], CLIENT_CONFIG_PROPERTY[i]});
        }

        for (int i = 0; i < clientProperties.size(); i++) {
            //if the property is correctly assign in the config file
            try {
                //System.out.println(this.getClass().getDeclaredField(clientProperties.).get(this));
                this.getClass().getDeclaredField(CLIENT_CONFIG_LIST[i][0].toString()).set(this, CLIENT_CONFIG_LIST[i][1]);
                System.out.println(this.getClass().getDeclaredField(CLIENT_CONFIG_LIST[i][0].toString()).get(this));

                System.out.println(CLIENT_CONFIG_LIST[i][0].toString()+CLIENT_CONFIG_LIST[i][1].toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LOGGER.log(Level.INFO, "{0} ({1}) successfully read on client.properties file", new Object[] {CLIENT_CONFIG_LIST[i][0], CLIENT_CONFIG_PROPERTY[i]});
        }
    }

    private void initClasses(String name) {
        menu = new Menu(this);
        playing = new Playing(this);
        connect = new Connect(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void update() {
        switch (GameStates.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case CONNECT:
                connect.update();
                break;
            case OPTIONS:
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch (GameStates.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case CONNECT:
                connect.draw(g);
                break;
            case OPTIONS:
            case QUIT:
            default:
                break;
        }
    }

    @Override
    /**
     * thread to update the game and the display at the right time
     */
    public void run() {
        double timePerUpdate = 1000000000.0 / UPSSET; //time between 2 update in nano second
        double timePerFrame = 1000000000.0 / fpsSet; //time between 2 image in nano second

        long previousTime = System.nanoTime(); //set a memory with the last time check

        double deltaU = 0; //store the time passed evry check if >= 1 it will create an update
        double deltaF = 0; //store the time passed evry check if >= 1 it will refresh the screen

        //display FPS & UPS
        long lastCheck = System.currentTimeMillis(); //
        int frames = 0;
        int updates = 0;


        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if(deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            //display FPS & UPS
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck += 1000; //add one second to the timer
                System.out.println(String.format("FPS: %s | UPS:%s", frames, updates)); //display values
                frames=0; //reset frame counter
                updates=0; //reset update counter
            }
        }
    }

    public Thread getGameThread() {
        return gameThread;
    }

    /**
     * when the window lost focus
     */
    public void windowLostFocus() {
        if(GameStates.state == GameStates.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    public Playing getPlaying() {
        return playing;
    }

    public void setPlaying(Playing playing) {
        this.playing = playing;
    }

    public Menu getMenu() {
        return menu;
    }

    public Connect getConnect() {
        return connect;
    }
}
