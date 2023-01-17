package com.jbmo60927.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.jbmo60927.levels.Level;
import com.jbmo60927.ui.DrawingImage;
import com.jbmo60927.App;

public class Drawing extends State implements StateMethods {

    private DrawingImage drawingImage = new DrawingImage();

    private com.jbmo60927.levels.Level level;

    public Drawing(App app) {
        super(app);
        initClasses();
    }

    private void initClasses() {
        level = new Level(30, 30);

    }

    public Level getLevelHandler() {
        return level;
    }

    @Override
    public void update() {
        level.update();
    }

    @Override
    public void draw(Graphics g) {
        level.draw(g); //level behind player
        drawingImage.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_D:
			break;
		case KeyEvent.VK_Q:
			break;
		case KeyEvent.VK_SPACE:
            break;
        case KeyEvent.VK_ESCAPE:
            GameStates.setGameState(GameStates.MENU);
            break;
        default:
            break;
		}
    }

    @Override
    public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
            case KeyEvent.VK_D:
                break;
            case KeyEvent.VK_Q:
                break;
            default:
                break;
		}
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        drawingImage.mouseWheelMoved(e);
    }
}