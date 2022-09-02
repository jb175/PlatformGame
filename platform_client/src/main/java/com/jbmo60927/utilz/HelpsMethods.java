package com.jbmo60927.utilz;

import com.jbmo60927.main.Game;
import java.awt.geom.Rectangle2D;

public class HelpsMethods {
    
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!IsSolid(x, y, lvlData))
			if (!IsSolid(x + width, y + height, lvlData))
				if (!IsSolid(x + width, y, lvlData))
					if (!IsSolid(x, y + height, lvlData))
						return true;
		return false;
	}

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
		if (x < 0 || x >= Game.GAME_WIDTH)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;

		float xIndex = x / Game.TILE_SIZE;
		float yIndex = y / Game.TILE_SIZE;

		int value = lvlData[(int) yIndex][(int) xIndex];

		if (value >= 48 || value < 0 || value != 11)
			return true;
		return false;
	}

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILE_SIZE);
        if(xSpeed > 0) { //wall on right
			int tileXPos = currentTile * Game.TILE_SIZE;
			int xOffset = (int) (Game.TILE_SIZE - hitbox.width);
			return tileXPos+xOffset-1;
		} else { //wall on left
			return currentTile * Game.TILE_SIZE;
		}
    }

    public static float GetEntityYPosUnderAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILE_SIZE);
        if(airSpeed > 0) { //falling on floor
			int tileYPos = currentTile * Game.TILE_SIZE;
			int YOffset = (int) (Game.TILE_SIZE - hitbox.height);
			return tileYPos+YOffset-1;
		} else { //jumping
			return currentTile * Game.TILE_SIZE;
		}
    }
	

	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		// Check the pixel below bottomleft and bottomright
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;

		return true;

	}
}
