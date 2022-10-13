package com.jbmo60927;

import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.gamestates.Connect;
import com.jbmo60927.gamestates.GameStates;
import com.jbmo60927.gamestates.Menu;
import com.jbmo60927.gamestates.Playing;
import com.jbmo60927.logger.MyLogger;
import com.jbmo60927.main.GamePanel;
import com.jbmo60927.main.GameWindow;
import com.jbmo60927.utilz.PropertyFile;

/**
 * Hello world!
 */
public final class App implements Runnable {

    //object to create the panel for the window
    private GamePanel gamePanel;

    //thread running continuously to display the game
    private Thread gameThread;

    private Playing playing;
    private Menu menu;
    private Connect connect;

    public static final int TILE_DEFAULT_SIZE = 32;
    public static final int TILE_IN_WIDTH = 26;
    public static final int TILE_IN_HEIGHT = 14;

    public static final String VERSION;
    public static final int FPSSET;
    public static final int UPSSET = 200;
    public static final Float SCALE;
    public static final String IP;
    public static final int PORT;
    public static final String NAME;
    

    public static final int TILE_SIZE;
    public static final int GAME_WIDTH;
    public static final int GAME_HEIGHT;

    private static PropertyFile propertyFile; //property file
    private static String propertyFileName = "com/jbmo60927/properties/client.xml"; //path for the property file

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    static {
        //read configuration file
        propertyFile = new PropertyFile(propertyFileName);

        //read properties from the file
        VERSION = readStringProperty(propertyFile, "version");
        FPSSET = readIntProperty(propertyFile, "fps");
        SCALE = readFloatProperty(propertyFile, "scale");
        IP = readStringProperty(propertyFile, "ip");
        PORT = readIntProperty(propertyFile, "port");
        NAME = readStringProperty(propertyFile, "name");

        //init window size values
        TILE_SIZE = (int)(TILE_DEFAULT_SIZE * SCALE);
        GAME_WIDTH = TILE_SIZE * TILE_IN_WIDTH;
        GAME_HEIGHT = TILE_SIZE * TILE_IN_HEIGHT;
    }
    
    private App() {
        LOGGER.setLevel(Level.INFO);


        initClasses();

        gamePanel = new GamePanel(this);
        new GameWindow(gamePanel, this);

        //set focus on pannel
        while (!gamePanel.isFocusOwner()) {
            gamePanel.requestFocus();
        }

        startGameLoop();
    }

    /**
     * read a string property
     * @param propertyFile the file from where we read the property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public static String readStringProperty(PropertyFile propertyFile, String name) {
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
    public static int readIntProperty(PropertyFile propertyFile, String name) {
        if (propertyFile.getProperties().getProperty(name) != null) {
            LOGGER.log(Level.CONFIG, () -> String.format("%s sucessfully read", name));
            try {
                return Integer.parseInt(propertyFile.getProperties().getProperty(name));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "cannot parse string property into an integer", e);
            }
        } else {
            LOGGER.log(Level.SEVERE, () -> String.format("%s cannot be read on the server.xml file", name));
            System.exit(1);
        }
        return 0;
    }

    /**
     * read a float property
     * @param propertyFile the file from where we read the property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public static float readFloatProperty(PropertyFile propertyFile, String name) {
        if (propertyFile.getProperties().getProperty(name) != null) {
            LOGGER.log(Level.CONFIG, () -> String.format("%s sucessfully read", name));
            try {
                return Float.parseFloat(propertyFile.getProperties().getProperty(name));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "cannot parse string property into an integer", e);
            }
        } else {
            LOGGER.log(Level.SEVERE, () -> String.format("%s cannot be read on the server.xml file", name));
            System.exit(1);
        }
        return 0f;
    }

    private void initClasses() {
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
        double timePerFrame = 1000000000.0 / FPSSET; //time between 2 image in nano second

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
