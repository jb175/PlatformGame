package com.jbmo60927.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.jbmo60927.App;
import com.jbmo60927.ui.Component;
import com.jbmo60927.ui.NumberInput;
import com.jbmo60927.ui.PointNumberInput;

public class Options extends State implements StateMethods {

    //components
    private int selectedComponent = -1;
    private Component[] components = new Component[4];

    //animations
    private int writeAnimationStatus = 0;
    private static final int UP_ANIMATION = 100;
    private int counter = 0;
    
    public Options(App game) {
        super(game);
        loadComponents();
    }

    private void loadComponents() {
        components[0] = new PointNumberInput(App.GAME_WIDTH/2, (int) (100 * App.SCALE), 50, "scale", Float.toString(app.modifiedScale));
        components[1] = new NumberInput(App.GAME_WIDTH/2, (int) (170 * App.SCALE), 50, "fps", Integer.toString(app.fpsSet));
        components[3] = new Component(App.GAME_WIDTH/2, (int) (300 * App.SCALE), 50, "save") {
            @Override
            public void mouseReleased(MouseEvent e) {
                GameStates.setGameState(GameStates.MENU);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
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
        // TODO Auto-generated method stub
        
    }
    
}
