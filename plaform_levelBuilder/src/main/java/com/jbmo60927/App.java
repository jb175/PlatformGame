package com.jbmo60927;

import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.gamestates.GameStates;
import com.jbmo60927.gamestates.Menu;
import com.jbmo60927.gamestates.Drawing;
import com.jbmo60927.logger.MyLogger;
import com.jbmo60927.main.GamePanel;
import com.jbmo60927.main.GameWindow;


public final class App implements Runnable {

    //object to create the panel for the window
    private GamePanel gamePanel;

    //thread running continuously to display the game
    private Thread gameThread;
    
    //all differents pages
    private Drawing drawing;
    private Menu menu;
    
    //tile constant
    public static final int TILE_DEFAULT_SIZE = 32;
    public static final int TILE_IN_WIDTH = 26;
    public static final int TILE_IN_HEIGHT = 14;

    public static final int FPS_SET = 120;
    public static final int UPS_SET = 200;
    public static final Float SCALE = 1.5f;

    private Boolean running;

    private static final boolean DISPLAYFPS = true;

    //value initialized when data is read from a file
    public static final int TILE_SIZE = (int)(TILE_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILE_SIZE * (TILE_IN_WIDTH+3);
    public static final int GAME_HEIGHT = TILE_SIZE * (TILE_IN_HEIGHT+1);

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    
    public App() {
        //set logger level
        LOGGER.setLevel(Level.INFO);

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

    private void initClasses() {
        menu = new Menu(this);
        drawing = new Drawing(this);
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
            case DRAWING:
                drawing.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch (GameStates.getGameState()) {
            case MENU:
                menu.draw(g);
                break;
            case DRAWING:
                drawing.draw(g);
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
        double timePerFrame = 1000000000.0 / FPS_SET; //time between 2 image in nano second
        int lastFps = FPS_SET;

        long previousTime = System.nanoTime(); //set a memory with the last time check

        double deltaU = 0; //store the time passed evry check if >= 1 it will create an update
        double deltaF = 0; //store the time passed evry check if >= 1 it will refresh the screen

        //display FPS & UPS
        long lastCheck = System.currentTimeMillis(); //
        int frames = 0;
        int updates = 0;

        this.running = true;


        while (Boolean.TRUE.equals(this.running)) {
            //to allow in-game fps modification
            if (lastFps != FPS_SET)
                timePerFrame = 1000000000.0 / FPS_SET;
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
                if (DISPLAYFPS)
                    System.out.println(String.format("FPS: %s | UPS:%s", frames, updates)); //display values
                frames=0; //reset frame counter
                updates=0; //reset update counter
            }
        }
    }

    /**
     * when the window lost focus
     */
    public void windowLostFocus() {
        if(GameStates.getGameState() == GameStates.DRAWING) {
            //drawing.resetDirBooleans();
        }
    }

    public Menu getMenu() {
        return menu;
    }
    
    public Drawing getDrawing() {
        return drawing;
    }

    public static void main( String[] args ) throws IOException {
        MyLogger.setup(); //setup the logger for the app
        new App(); //begin the app
    }
}
