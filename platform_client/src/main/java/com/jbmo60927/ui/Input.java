package com.jbmo60927.ui;

import java.awt.event.KeyEvent;

public abstract class Input extends Component {
    
    protected String name, value;

    public Input(int xPos, int yPos, int size, String name, String defaultValue) {
        super(xPos, yPos, size, name+": "+defaultValue+"  ");
        this.name = name;
        this.value = defaultValue;
    }

    @Override
    public void update() {
        super.update();
        text = name+": "+value+"  ";
    }

    @Override
    public void selectedUpdate(int writeAnimationStatus) {
        super.selectedUpdate(writeAnimationStatus);
        if (writeAnimationStatus==0) {
            text = name+": "+value+"  ";
        } else {
            text = name+": "+value+"_";
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void addToValue(char c) {
        this.value += c;
    }

    public void removeToValue() {
        if (this.value.length() > 0)
            this.value = this.value.substring(0, this.value.length()-1);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
                this.removeToValue();
                return;
            default:
                break;
        }
    }
}
