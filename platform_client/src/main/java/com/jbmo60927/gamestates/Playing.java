package com.jbmo60927.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;

import com.jbmo60927.entities.MovablePlayer;
import com.jbmo60927.entities.OtherPlayer;
import com.jbmo60927.levels.LevelHandler;
import com.jbmo60927.App;

public class Playing extends State implements StateMethods {

    private MovablePlayer player;
    private HashMap<Integer, OtherPlayer> players = new HashMap<>();
    private LevelHandler LevelHandler;

    public Playing(App app) {
        super(app);
        initClasses();
    }

    private void initClasses() {
        LevelHandler = new LevelHandler(app);
        player = new MovablePlayer(130*App.SCALE, 130*App.SCALE);
        player.loadLvlData(LevelHandler.getCurrentLevel().getLvlData());
    }

    public MovablePlayer getPlayer() {
        return player;
    }

    public HashMap<Integer, OtherPlayer> getPlayers() {
        return players;
    }

    @Override
    public void update() {
        //update the player
        player.update();
        //update all other player
        for (Iterator<Integer> it = players.keySet().iterator(); it.hasNext(); ) {
            int index = it.next();
            players.get(index).update();
        }
        LevelHandler.update();
        //send updates to other players
        app.getConnect().getGameLinkThread().sendUpdates();
    }

    @Override
    public void draw(Graphics g) {
        Boolean showHitbox = true;
        LevelHandler.draw(g); //level behind player
        player.render(g, showHitbox);
        for (Iterator<Integer> it = players.keySet().iterator(); it.hasNext(); ) {
            int index = it.next();
            players.get(index).render(g, showHitbox);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			player.setAttacking(true);
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
		// case KeyEvent.VK_Z:
		// 	player.setUp(true);
		// 	break;
		// case KeyEvent.VK_S:
		// 	player.setDown(true);
		// 	break;
		case KeyEvent.VK_D:
			player.setRight(true);
			break;
		case KeyEvent.VK_Q:
			player.setLeft(true);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(true);
            break;
        case KeyEvent.VK_ESCAPE:
            app.getConnect().getGameLinkThread().close();
            //wait until disconnection
            long time = System.currentTimeMillis();
            while (app.getConnect().isConnected()) {
                if (System.currentTimeMillis() > time+10000) {
                    throw new Error("Can't disconnect player");
                }
                //System.out.println(game.getConnect().isConnected());
            }
            app.setPlaying(new Playing(app));
            GameStates.state = GameStates.MENU;
            break;
		}
    }

    @Override
    public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		// case KeyEvent.VK_Z:
		// 	player.setUp(false);
		// 	break;
		// case KeyEvent.VK_S:
		// 	player.setDown(false);
		// 	break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
		case KeyEvent.VK_Q:
			player.setLeft(false);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(false);
            break;
		}
		//System.out.println(e.getKeyCode());
    }
}
