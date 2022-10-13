package com.jbmo60927.ui;

import java.awt.event.MouseEvent;

import com.jbmo60927.gamestates.GameStates;
import com.jbmo60927.main.Game;

public class Button extends Component {

    GameStates state;

    public Button(int xPos, int yPos, int size, String text, GameStates state) {
        super(xPos, yPos, size, text);
        this.state = state;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GameStates.state = state;
    }

    @Override
    public void update() {
        super.update();
        
        if(4*Game.scale*size-metrics.stringWidth(text) < 0) {
            while (4*Game.scale*size-metrics.stringWidth(text) < 16) {
                text=text.substring(0, text.length()-2);
            }
            text +="..";
        }
    }

    @Override
    public void selectedUpdate() {
        super.selectedUpdate();

        if(4*Game.scale*size-metrics.stringWidth(text) < 0) {
            while (4*Game.scale*size-metrics.stringWidth(text) < 16) {
                text=text.substring(0, text.length()-2);
            }
            text +="..";
        }
    }
}
