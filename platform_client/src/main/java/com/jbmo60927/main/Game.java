package com.jbmo60927.main;

import java.awt.Graphics;

import com.jbmo60927.gamestates.Connect;
import com.jbmo60927.gamestates.GameStates;
import com.jbmo60927.gamestates.Menu;
import com.jbmo60927.gamestates.Playing;

public class Game implements Runnable {

    private GamePanel gamePanel;
    private Thread gameThread;
    private final int UPS_SET = 200;
    private final int FPS_SET = 120;

    private Playing playing;
    private Menu menu;
    private Connect connect;

    public final static int TILE_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILE_IN_WIDTH = 26;
    public final static int TILE_IN_HEIGHT = 14;
    public final static int TILE_SIZE = (int)(TILE_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILE_SIZE * TILE_IN_WIDTH;
    public final static int GAME_HEIGHT = TILE_SIZE * TILE_IN_HEIGHT;


    public Game() {
        initClasses("test");

        gamePanel = new GamePanel(this);
        new GameWindow(gamePanel, this);

        //set focus on pannel
        while (!gamePanel.isFocusOwner()) {
            gamePanel.requestFocus();
        }

        startGameLoop();
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
        double timePerUpdate = 1000000000.0 / UPS_SET; //time between 2 update in nano second
        double timePerFrame = 1000000000.0 / FPS_SET; //time between 2 image in nano second

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
