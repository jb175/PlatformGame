package com.jbmo60927.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import com.jbmo60927.entities.MovablePlayer;
import com.jbmo60927.entities.OtherPlayer;
import com.jbmo60927.levels.LevelHandler;
import com.jbmo60927.App;

public class Playing extends State implements StateMethods {

    private MovablePlayer player;
    private HashMap<Integer, OtherPlayer> players = new HashMap<>();
    private LevelHandler levelHandler;

    public Playing(App app) {
        super(app);
        initClasses();
    }

    private void initClasses() {
        levelHandler = new LevelHandler(app);
        player = new MovablePlayer(130*App.SCALE, 130*App.SCALE);
        player.loadLvlData(levelHandler.getCurrentLevel().getLvlData());
    }

    public MovablePlayer getPlayer() {
        return player;
    }

    public HashMap<Integer, OtherPlayer> getPlayers() {
        return players;
    }

    public LevelHandler getLevelHandler() {
        return levelHandler;
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
        levelHandler.update();
        //send updates to other players
        // app.getConnect().getGameLinkThread().sendUpdates();
    }

    @Override
    public void draw(Graphics g) {
        levelHandler.draw(g); //level behind player
        player.render(g, app.hitbox);
        for (Iterator<Integer> it = players.keySet().iterator(); it.hasNext(); ) {
            int index = it.next();
            players.get(index).render(g, app.hitbox);
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
            //app.getConnect().getGameLinkThread().sendPacket(new SendQuitPacket());
            //wait until disconnection
            long time = System.currentTimeMillis();
            while (app.getConnect().isConnected()) {
                if (System.currentTimeMillis() > time+3000)
                    LOGGER.log(Level.SEVERE, "Cant disconnect player");
            }
            app.setPlaying(new Playing(app));
            GameStates.setGameState(GameStates.MENU);
            break;
        default:
            String msg = String.format("key pressed %d named %s", e.getKeyCode(), java.awt.event.KeyEvent.getKeyText(e.getKeyCode()));
            LOGGER.log(Level.FINE, msg);
		}
    }

    @Override
    public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_Q:
                player.setLeft(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
            default:
                break;
		}
    }

    public void changeCurrentLevel(int levelNumber) {
        if(levelNumber >= 0 && levelNumber < 10 && levelHandler.getLevels()[levelNumber+1] != null) {
            levelHandler.setCurrentLevel(levelNumber+1);
        } else {
            levelHandler.setCurrentLevel(0);
        }
        player.loadLvlData(levelHandler.getCurrentLevel().getLvlData());
    }
}