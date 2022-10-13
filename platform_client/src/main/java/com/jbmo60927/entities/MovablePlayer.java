package com.jbmo60927.entities;

import static com.jbmo60927.utilz.Constants.PlayerConstants.*;
import static com.jbmo60927.utilz.HelpsMethods.CanMoveHere;
import static com.jbmo60927.utilz.HelpsMethods.GetEntityXPosNextToWall;
import static com.jbmo60927.utilz.HelpsMethods.GetEntityYPosUnderAboveFloor;
import static com.jbmo60927.utilz.HelpsMethods.IsEntityOnFloor;

import com.jbmo60927.App;

public class MovablePlayer extends Player {

    //movements
    private boolean moving = false, attacking = false; //is player moving
    private boolean right, left, jump; //player moving directions
    private final float playerSped = 2.0f; //player speed
    private int[][] lvlData;

    //jumping
    private float airSpeed = 0f; //the actual speed of the player in air
    private final float jumpSpeed = -2.25f * App.scale; //air initial speed
    private final float gravity = 0.04f * App.scale; //air speed decrese with gravity
    private float fallSpeedAfterCollision = 0.5f * App.scale; //speed at collision
    private boolean inAir = false; //is the player in air

    public MovablePlayer(float x, float y) {
        super(x, y, "");
    }

    @Override
    /**
     * update the player (movement and animations)
     */
    public void update() {
		updatePos(); //update the position of the player depending on the key pressed
		setAnimation(); //set the type of animation we need to display
		updateAnimationTick(); //update the animation
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    /**
     * update the player position depending on which key is pressed or not
     */
	private void updatePos() {
        moving = false;
        if (jump) {
            jump();
        }
        if (!left && !right && !inAir) {
            return;
        }

        float xSpeed = 0;

        if (right) //to go right
            xSpeed += playerSped;
        else if (left) //to go left
            xSpeed -= playerSped;
        
            

		if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
        
        if (inAir) {
           
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderAboveFloor(hitbox, airSpeed);
                if(airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        } else
            updateXPos(xSpeed);
        moving = true;
	}

    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    /**
     * set the animation to be displayed
     */
	private void setAnimation() {
        int startAni = playerAction;

		if (moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;

        if (inAir) {
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }
        
        if (attacking)
            playerAction = ATTACK_1;
        
        if(startAni != playerAction)
            resetAniTick();
	}

    /**
     * reset anitick and index to get the full animation
     */
    private void resetAniTick() {
        aniIndex=0;
        aniTick=0;
    }

    /**
     * update the animation
     */
    @Override
	protected void updateAnimationTick() {
		aniTick++; //we increment the stored tick value for animation
		if (aniTick >= aniSpeed) { //if this value goes over the value predefined
			aniTick = 0; //we reset the number of tick stored
			aniIndex++; //we increase the index to view the nesxt image
		}
        //out of the boucle because if the animation change and the print image is not defined for this animation we should hide it
        if (aniIndex >= GetSpriteAmount(playerAction)) { //if the value is bigger than the total number of image
            aniIndex = 0; //we display the first image of the animation
            attacking = false;
        }
	}

    public void setRight(final boolean right) {
        this.right = right;
    }

    public void setLeft(final boolean left) {
        this.left = left;
    }

    public void setJump(final boolean jump) {
        this.jump = jump;
    }

    /**
     * stop movement => reset all movements
     */
    public void resetDirBooleans() {
        right = false;
        left = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public String getData() {
        return String.valueOf(hitbox.x)+" "+String.valueOf(hitbox.y)+" "+Integer.toString(playerAction);
    }

    public void setName(String name) {
        this.name = name;
    }
}
