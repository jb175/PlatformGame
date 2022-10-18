package com.jbmo60927.utilz;

import com.jbmo60927.App;
import java.awt.geom.Rectangle2D;

public class HelpsMethods {
	private HelpsMethods() {
	  throw new IllegalStateException("Utility class");
	}
    
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!isSolid(x, y, lvlData))
			if (!isSolid(x + width, y + height, lvlData))
				if (!isSolid(x + width, y, lvlData))
					if (!isSolid(x, y + height, lvlData))
						return true;
		return false;
	}

    private static boolean isSolid(float x, float y, int[][] lvlData) {
		if (x < 0 || x >= App.GAME_WIDTH)
			return true;
		if (y < 0 || y >= App.GAME_HEIGHT)
			return true;

		float xIndex = x / App.TILE_SIZE;
		float yIndex = y / App.TILE_SIZE;

		int value = lvlData[(int) yIndex][(int) xIndex];

		return value >= 48 || value < 0 || value != 11;
	}

    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / App.TILE_SIZE);
        if(xSpeed > 0) { //wall on right
			int tileXPos = currentTile * App.TILE_SIZE;
			int xOffset = (int) (App.TILE_SIZE - hitbox.width);
			return (float) tileXPos+xOffset-1;
		} else { //wall on left
			return (float) currentTile * App.TILE_SIZE;
		}
    }

    public static float getEntityYPosUnderAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / App.TILE_SIZE);
        if(airSpeed > 0) { //falling on floor
			int tileYPos = currentTile * App.TILE_SIZE;
			int yOffset = (int) (App.TILE_SIZE - hitbox.height);
			return (float) tileYPos+yOffset-1;
		} else { //jumping
			return (float) currentTile * App.TILE_SIZE;
		}
    }
	

	public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		// Check the pixel below bottomleft and bottomright
		if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;

		return true;

	}

	
}
