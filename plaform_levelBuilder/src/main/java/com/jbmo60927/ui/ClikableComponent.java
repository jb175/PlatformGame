package com.jbmo60927.ui;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class ClikableComponent extends Component {

    protected Rectangle bounds;

    protected ClikableComponent(int xPos, int yPos, int width, int height, BufferedImage[] images) {
        super(xPos, yPos, width, height, images);
        initBounds();
    }

    private void initBounds() {
        this.bounds = new Rectangle(this.xPos, this.yPos, this.width, this.height);
    }
}
