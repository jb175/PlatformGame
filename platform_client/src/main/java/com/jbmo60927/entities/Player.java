package com.jbmo60927.entities;

import static com.jbmo60927.utilz.Constants.PlayerConstants.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.FontMetrics;

import com.jbmo60927.App;
import com.jbmo60927.utilz.LoadSave;

/**
 * player: a class used by every player herited from entities that contain everything to control and display the player 
 */
public class Player extends Entity {

    //animations
	private BufferedImage[][] animations; //store all image for player animation   first array => animations   second array => images
	protected int playerAction = IDLE; //current action from the player => defined the displayed animation
	protected int aniTick, aniIndex; //animation variables   aniTick count the update an image has received   aniIndex store the actual displayed image from an animation
    protected final int aniSpeed = 25; //animation variables   anispeed is the number of tick an image while be displayed (all images from animations stay the same time)
    protected String name; //name of the player
    private float xDrawOffset = 21 * App.scale, yDrawOffset = 4 * App.scale;

    //images
    private final static int PLAYER_DEFAULT_WIDTH = 64, PLAYER_DEFAULT_HEIGH = 40;

    /**
     * set default values for a player
     * 
     * @param x position x
     * @param y position y
     */
    public Player(final float x, final float y, String name) {
        super(x, y, (int) (64 * App.scale), (int) (40 * App.scale)); //send default position to entities
        loadAnimations(); //load all animations
		initHitbox(x, y, 20 * App.scale, 28 * App.scale);
    }

    /**
     * load all image for the animations
     */
	private void loadAnimations() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[9][6]; //setup size on array to store all images from animations
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i * PLAYER_DEFAULT_WIDTH, j * PLAYER_DEFAULT_HEIGH, PLAYER_DEFAULT_WIDTH, PLAYER_DEFAULT_HEIGH); //store the sub image on arrays
	}
    
    /**
     * update the player (movement and animations)
     */
    public void update() {
		updateAnimationTick(); //update the animation
    }
    
    /**
     * display the player
     * @param g object where we draw images
     */
    public void render(final Graphics g, Boolean showHitbox) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null); //display the good image at the good position with the good size
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(name, (int) (hitbox.x - xDrawOffset) + (width - metrics.stringWidth(name)) / 2, (int) (hitbox.y - yDrawOffset));
        if (showHitbox) {
            drawHitbox(g);
        }
    }

    /**
     * update the animation
     */
	protected void updateAnimationTick() {
		aniTick++; //we increment the stored tick value for animation
		if (aniTick >= aniSpeed) { //if this value goes over the value predefined
			aniTick = 0; //we reset the number of tick stored
			aniIndex++; //we increase the index to view the nesxt image
		}
        //out of the boucle because if the animation change and the print image is not defined for this animation we should hide it
        if (aniIndex >= GetSpriteAmount(playerAction)) { //if the value is bigger than the total number of image
            aniIndex = 0; //we display the first image of the animation
        }
	}

    public String getName() {
        return name;
    }
}
