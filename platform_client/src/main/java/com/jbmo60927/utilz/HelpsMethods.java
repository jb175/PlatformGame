package com.jbmo60927.utilz;

import com.jbmo60927.App;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

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

	/**
	 * return a byte array from the int
	 * @param value the value we wants to convert into bytes
	 * @param bytesSize the number of bytes we wants [1-4]
	 * @return the array
	 */
	public static byte[] intToBytes(int value, int bytesSize) {
		byte[] array = new byte[bytesSize];
		switch (bytesSize) {
			case 4:
				array[0] = (byte)((value >> 24) & 0xff);
				array[1] = (byte)((value >> 16) & 0xff);
				array[2] = (byte)((value >> 8) & 0xff);
				array[3] = (byte)((value >> 0) & 0xff);
				break;
			case 3:
				array[0] = (byte)((value >> 16) & 0xff);
				array[1] = (byte)((value >> 8) & 0xff);
				array[2] = (byte)((value >> 0) & 0xff);
				break;
			case 2:
				array[0] = (byte)((value >> 8) & 0xff);
				array[1] = (byte)((value >> 0) & 0xff);
				break;
			case 1:
				array[0] = (byte)(value & 0xff);
				break;
			default:
				break;
		}
		return array;
	}

    /**
	 * return an int from a byte array
	 * @param array the byte array we wants to convert (1-4 bytes)
	 * @return the corresponding int
	 */
	public static int bytesToInt(byte[] array) {
		int value = 0;
		switch (array.length) {
			case 4:
                value = ((array[0]) << 24) + ((array[1]) << 16) + ((array[2]) << 8) + (array[3] & 0xff);
				break;
			case 3:
                value = ((array[0]) << 16) + ((array[1]) << 8) + (array[2] & 0xff);
				break;
			case 2:
                value = ((array[0]) << 8) + (array[1] & 0xff);
				break;
			case 1:
                value = (array[0] & 0xff);
				break;
			default:
				break;
		}
		return value;
	}
}
