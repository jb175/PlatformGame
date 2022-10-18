package com.jbmo60927.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Level;

import com.jbmo60927.App;
import com.jbmo60927.ui.Button;
import com.jbmo60927.ui.Component;


public class Menu extends State implements StateMethods {

    private int selectedComponent;
    private Component[] components = new Component[3];


    public Menu(App app) {
        super(app);
        LoadComponents();
        selectedComponent = -1;
    }

    private void LoadComponents() {
        components[0] = new Button(App.GAME_WIDTH/2, (int) (150 * App.SCALE), 25, "play", GameStates.CONNECT);
        components[1] = new Button(App.GAME_WIDTH/2, (int) (220 * App.SCALE), 25, "options", GameStates.OPTIONS);
        components[2] = new Button(App.GAME_WIDTH/2, (int) (290 * App.SCALE), 25, "quit", GameStates.QUIT);
    }

    @Override
    public void update() {
        for (int i = 0; i < components.length; i++) {
            if (i==selectedComponent) {
                components[i].selectedUpdate();
            } else {
                components[i].update();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        for (Component c : components)
            c.draw(g);
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
                    State.LOGGER.log(Level.FINE, Integer.toString(e.getKeyCode()));
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
