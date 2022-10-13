package com.jbmo60927.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.jbmo60927.App;
import com.jbmo60927.main.GameLinkThread;
import com.jbmo60927.ui.Component;
import com.jbmo60927.ui.Input;
import com.jbmo60927.ui.LetterNumberInput;
import com.jbmo60927.ui.NumberInput;
import com.jbmo60927.ui.PointNumberInput;

public class Connect extends State implements StateMethods {

    private boolean connected = false;

    private GameLinkThread gameLinkThread;
    private BufferedWriter os;
    private Socket socketOfClient;
    private BufferedReader is;
    
    //components
    private int selectedComponent = -1;
    private Component[] components = new Component[4];

    //animations
    private int writeAnimationStatus = 0;
    private final int UP_ANIMATION = 100;
    private int counter = 0;

    private App app;

    public Connect(App app) {
        super(app);
        this.app = app;
        LoadComponents();
    }


    private void LoadComponents() {
        components[0] = new PointNumberInput(App.GAME_WIDTH/2, (int) (100 * App.scale), 50, "ip adress", "188.165.242.225");
        components[1] = new NumberInput(App.GAME_WIDTH/2, (int) (170 * App.scale), 50, "port", "7777");
        components[2] = new LetterNumberInput(App.GAME_WIDTH/2, (int) (240 * App.scale), 50, "name", "jb");
        components[3] = new Component(App.GAME_WIDTH/2, (int) (300 * App.scale), 50, "play") {
            @Override
            public void mouseReleased(MouseEvent e) {
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

        //try connection
        try {
            app.getPlaying().getPlayer().setName(((Input)components[2]).getValue());
            // Send a request to connect to the server is listening
            // on machine given ip address port 7777.
            socketOfClient = new Socket(((Input)components[0]).getValue(), Integer.parseInt(((Input)components[1]).getValue()));

            gameLinkThread = new GameLinkThread(socketOfClient, app);

            gameLinkThread.start();
            connected = true;
            GameStates.state = GameStates.PLAYING;

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + ((Input)components[0]).getValue());
            return;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + ((Input)components[0]).getValue());
            return;
        } catch (ClassCastException e) {
            System.err.println("Input type error");
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
                //System.out.println(e.getKeyCode());
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
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
