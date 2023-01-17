package com.jbmo60927.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.jbmo60927.App;
import com.jbmo60927.ui.UserInput;
import com.jbmo60927.ui.NumberInput;
import com.jbmo60927.ui.Input;


public class Menu extends State implements StateMethods {

    private int selectedComponent = -1;
    private UserInput[] components = new UserInput[3];

    //animations
    private int writeAnimationStatus = 0;
    private static final int UP_ANIMATION = 100;
    private int counter = 0;

    public Menu(App app) {
        super(app);
        LoadComponents();
    }

    private void LoadComponents() {
        components[0] = new NumberInput(App.GAME_WIDTH/2, (int) (170 * App.SCALE), 50, "Heigh", Integer.toString(50));
        components[1] = new NumberInput(App.GAME_WIDTH/2, (int) (220 * App.SCALE), 50, "Weight", Integer.toString(50));
        components[2] = new UserInput(App.GAME_WIDTH/2, (int) (300 * App.SCALE), 50, "create Level") {
            @Override
            public void mouseReleased(MouseEvent e) {
                drawLevel();
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
        for (UserInput c : components)
            c.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (UserInput c : components) {
            if(isIn(e, c)) {
                c.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (int i = 0; i < components.length; i++) {
            UserInput c = components[i];
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
        for (UserInput c : components)
            c.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (UserInput c : components)
            c.setMouseOver(false);

        for (UserInput c : components)
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

    public void drawLevel() {
        int height = Integer.parseInt(((Input)components[0]).getValue());
        int weight = Integer.parseInt(((Input)components[1]).getValue());

        if (height>0 && weight>0) {
            app.getDrawing().getLevelHandler().setLvlData(height, weight);
            GameStates.setGameState(GameStates.DRAWING);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // TODO Auto-generated method stub
        
    }
}
