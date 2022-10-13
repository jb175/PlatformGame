package com.jbmo60927.utilz;

/**
* Constants for the server.
*
* @author jbmo60927
* @version 1.0
*/

public final class Constants {
	private Constants() {}

	/**
	* Trame signification.
	*
	* @author jbmo60927
	* @version 1.0
	*/

	public static final class TrameName {
		private TrameName() {}
		public static final int INITPLAYER 		= 0;
		public static final int INITDATA 		= 1;
		public static final int NEWPLAYER 		= 2;
		public static final int REMOVEPLAYER 	= 3;
		public static final int QUIT			= 4;
		public static final int OK 				= 5;
		public static final int PLAYERUPDATE 	= 6;
	}

	/**
	* Direction signification.
	*
	* @author jbmo60927
	* @version 1.0
	*/

	public static final class Directions {
		private Directions() {}
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	/**
	* Player animation.
	*
	* @author jbmo60927
	* @version 1.0
	*/

	public static final class PlayerConstants {
		private PlayerConstants() {}
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int GROUND = 4;
		public static final int HIT = 5;
		public static final int ATTACK_1 = 6;
		public static final int ATTACK_JUMP_1 = 7;
		public static final int ATTACK_JUMP_2 = 8;

		/**
		 * Number of image per animation.
		 * 
		 * @authoor jbmo60927
		 * @version 1.0
		 * @param playerAction for each action
		 * @return tick per animation
		 */
		public static int getSpriteAmount(int playerAction) {
			switch (playerAction) {
				case RUNNING:
					return 6;
				case IDLE:
					return 5;
				case HIT:
					return 4;
				case JUMP:
				case ATTACK_1:
				case ATTACK_JUMP_1:
				case ATTACK_JUMP_2:
					return 3;
				case GROUND:
					return 2;
				case FALLING:
				default:
					return 1;
			}
		}
	}
}
