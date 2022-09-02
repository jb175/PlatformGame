package com.jbmo60927.ui;

import java.awt.event.KeyEvent;

public class PointNumberInput extends Input {

    public PointNumberInput(int xPos, int yPos, int size, String name, String defaultValue) {
        super(xPos, yPos, size, name, defaultValue);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        switch (e.getKeyChar()) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '.':
                this.addToValue(e.getKeyChar());
                return;
            default:
                break;
        }
    }
    
}
