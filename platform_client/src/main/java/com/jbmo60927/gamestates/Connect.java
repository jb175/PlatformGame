package com.jbmo60927.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;

import com.jbmo60927.App;
import com.jbmo60927.main.GameLinkThread;
import com.jbmo60927.ui.Component;
import com.jbmo60927.ui.Input;
import com.jbmo60927.ui.LetterNumberInput;
import com.jbmo60927.ui.NumberInput;
import com.jbmo60927.ui.PointNumberInput;

public class Connect extends State implements StateMethods {

    private boolean isConnected = false;

    private GameLinkThread gameLinkThread;
    private BufferedWriter os;
    private Socket socketOfClient;
    private BufferedReader is;
    
    //components
    private int selectedComponent = -1;
    private Component[] components = new Component[4];

    //animations
    private int writeAnimationStatus = 0;
    private static final int UP_ANIMATION = 100;
    private int counter = 0;

    public Connect(App app) {
        super(app);
        loadComponents();
    }


    private void loadComponents() {
        components[0] = new PointNumberInput(App.GAME_WIDTH/2, (int) (100 * App.SCALE), 50, "ip adress", app.ip);
        components[1] = new NumberInput(App.GAME_WIDTH/2, (int) (170 * App.SCALE), 50, "port", Integer.toString(app.port));
        components[2] = new LetterNumberInput(App.GAME_WIDTH/2, (int) (240 * App.SCALE), 50, "name", app.name);
        components[3] = new Component(App.GAME_WIDTH/2, (int) (300 * App.SCALE), 50, "play") {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!"".equals(((Input)components[2]).getValue()))
                    connect();
            }
        };
    }

    @Override
    public void update() {
        for (int i = 0; i < components.length; i++) {
            if (i==selectedComponent) {
                components[i].selectedUpdate(writeAnimationStatus);
            } else {
                components[i].update();
            }
        }
        //animation
        counter++;
        if (counter > UP_ANIMATION) {
            counter = 0;
            writeAnimationStatus+=1;
            if (writeAnimationStatus > 1)
                writeAnimationStatus = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        for (Component c : components)
            c.draw(g);
    }

    private void connect() {
        app.ip = ((Input)components[0]).getValue();
        app.port = Integer.parseInt(((Input)components[1]).getValue());
        app.name = ((Input)components[2]).getValue();

        //try connection
        try {
            app.getPlaying().getPlayer().setName(app.name);
            // Send a request to connect to the server is listening
            // on machine given ip address port 7777.
            socketOfClient = new Socket(app.ip, app.port);

            gameLinkThread = new GameLinkThread(socketOfClient, app);

            gameLinkThread.start();
            isConnected = true;
            GameStates.setGameState(GameStates.PLAYING);

        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, String.format("Don\'t know about host %s", app.ip), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Couldn\'t get I/O for the connection to %s", app.ip), e);
        } catch (ClassCastException e) {
            LOGGER.log(Level.SEVERE, "Input type error", e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Component c : components) {
            if(isIn(e, c)) {
                c.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (int i = 0; i < components.length; i++) {
            Component c = components[i];
            if(isIn(e, c)) {
                if (c.isMousePressed()) {
                    c.mouseReleased(e);
                    selectedComponent = i;
                }
                break;
            }
        }
        resetComponents();
    }


    private void resetComponents() {
        for (Component c : components)
            c.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Component c : components)
            c.setMouseOver(false);

        for (Component c : components)
            if(isIn(e, c)) {
                c.setMouseOver(true);
                break;
            }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_TAB:
                selectedComponent++;
                if (selectedComponent >= components.length)
                    selectedComponent = 0;
                break;
            case KeyEvent.VK_ENTER:
                if (0 <= selectedComponent && selectedComponent < components.length)
                    components[selectedComponent].mouseReleased(null);
                else
                    selectedComponent = 0;
                break;
            default:
                if (selectedComponent >= 0)
                    components[selectedComponent].keyPressed(e);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public BufferedWriter getOs() {
        return os;
    }

    public Socket getSocketOfClient() {
        return socketOfClient;
    }

    public BufferedReader getIs() {
        return is;
    }

    public GameLinkThread getGameLinkThread() {
        return gameLinkThread;
    }

    public boolean isConnected() {
        return isConnected;
    }


    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

}
