package com.jbmo60927;

import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.gamestates.Connect;
import com.jbmo60927.gamestates.GameStates;
import com.jbmo60927.gamestates.Menu;
import com.jbmo60927.gamestates.Options;
import com.jbmo60927.gamestates.Playing;
import com.jbmo60927.logger.MyLogger;
import com.jbmo60927.main.GamePanel;
import com.jbmo60927.main.GameWindow;
import com.jbmo60927.utilz.PropertyFile;

/**
 * Game app central part
 */
public final class App implements Runnable {

    //object to create the panel for the window
    private GamePanel gamePanel;

    //thread running continuously to display the game
    private Thread gameThread;

    //all differents pages
    private Playing playing;
    private Menu menu;
    private Connect connect;
    private Options options;

    //tile constant
    public static final int TILE_DEFAULT_SIZE = 32;
    public static final int TILE_IN_WIDTH = 26;
    public static final int TILE_IN_HEIGHT = 14;

    //constants that can be read from a file
    public static final String VERSION = "1.2";
    public int fpsSet;
    public static final int UPS_SET = 200;
    public static final Float SCALE;
    public Float modifiedScale;
    public String ip;
    public int port;
    public String name;
    public Boolean hitbox;

    private static final boolean DISPLAYFPS = false;
    
    //value initialized when data is read from a file
    public static final int TILE_SIZE;
    public static final int GAME_WIDTH;
    public static final int GAME_HEIGHT;

    //property file
    private static PropertyFile propertyFile;
    //path for the property file
    private static String propertyFileName = "client.xml";

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    //executed with the game launch to read properties
    static {
        //read configuration file
        propertyFile = new PropertyFile(propertyFileName);

        SCALE = propertyFile.readFloatProperty("scale");

        //init window size values
        TILE_SIZE = (int)(TILE_DEFAULT_SIZE * SCALE);
        GAME_WIDTH = TILE_SIZE * TILE_IN_WIDTH;
        GAME_HEIGHT = TILE_SIZE * TILE_IN_HEIGHT;
    }
    
    /**
     * initialize the client.
     */
    private App() {
        //set logger level
        LOGGER.setLevel(Level.INFO);

        loadData();

        //init classes
        initClasses();

        //setup the display
        gamePanel = new GamePanel(this);
        new GameWindow(gamePanel, this);

        //set focus on the pannel
        while (!gamePanel.isFocusOwner()) {
            gamePanel.requestFocus();
        }

        startGameLoop();
    }

    /**
     * load data from XML file
     */
    public void loadData() {

        //read properties from the file
        fpsSet = propertyFile.readIntProperty("fps");
        modifiedScale = App.SCALE;
        ip = propertyFile.readStringProperty("ip");
        port = propertyFile.readIntProperty("port");
        name = propertyFile.readStringProperty("name");
        hitbox = propertyFile.readBooleanProperty("hitbox");
    }

    /**
     * save data to XML file at the end of the app
     */
    public void saveData() {
        propertyFile.saveIntProperty("fps", fpsSet);
        propertyFile.saveFloatProperty("scale", modifiedScale);
        propertyFile.saveStringProperty("ip", ip);
        propertyFile.saveIntProperty("port", port);
        propertyFile.saveStringProperty("name", name);
        propertyFile.saveBooleanProperty("hitbox", hitbox);
        propertyFile.saveFile();
        LOGGER.log(Level.INFO, "properties sucessfully register");
    }

    public static void saveProperty(PropertyFile propertyFile, String name, String value) {
        propertyFile.getProperties().setProperty(name, value);
    }

    private void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
        connect = new Connect(this);
        options = new Options(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void update() {
        switch (GameStates.getGameState()) {
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
                options.update();
                break;
            case QUIT:
            default:
                saveData();
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch (GameStates.getGameState()) {
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
                options.draw(g);
                break;
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
        double timePerUpdate = 1000000000.0 / UPS_SET; //time between 2 update in nano second
        double timePerFrame = 1000000000.0 / fpsSet; //time between 2 image in nano second
        int lastFps = fpsSet;

        long previousTime = System.nanoTime(); //set a memory with the last time check

        double deltaU = 0; //store the time passed evry check if >= 1 it will create an update
        double deltaF = 0; //store the time passed evry check if >= 1 it will refresh the screen

        //display FPS & UPS
        long lastCheck = System.currentTimeMillis(); //
        int frames = 0;
        int updates = 0;

        Boolean running = true;


        while (Boolean.TRUE.equals(running)) {
            //to allow in-game fps modification
            if (lastFps != fpsSet)
                timePerFrame = 1000000000.0 / fpsSet;
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
            if (DISPLAYFPS && System.currentTimeMillis() - lastCheck >= 1000) {
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
        if(GameStates.getGameState() == GameStates.PLAYING)
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

    /**
     * Run the client for the game Platform.
     * @param args The arguments of the program.
     * @throws IOException When the logger can't be created.
     */
    public static void main(String[] args) throws IOException {
        MyLogger.setup(); //setup the logger for the app
        new App(); //begin the app
    }
}
