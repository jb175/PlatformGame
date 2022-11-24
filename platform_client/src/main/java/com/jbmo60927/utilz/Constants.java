package com.jbmo60927.utilz;

import com.jbmo60927.App;

public class Constants {
	private static String errorMessage = "Utility class";

	private Constants() {
	  	throw new IllegalStateException(Constants.errorMessage);
	}

	public static class TrameName {
		private TrameName() {
		  	throw new IllegalStateException(Constants.errorMessage);
		}

		public static final int INITPLAYER 		= 0; //send
		public static final int INITDATA 		= 1; //received
		public static final int NEWPLAYER 		= 2; //send / received
		public static final int REMOVEPLAYER 	= 3; //received
		public static final int QUIT			= 4; //send
		public static final int OK 				= 5; //received
		public static final int PLAYERUPDATE 	= 6; //send / received
	}

	public static class UI{
		private UI() {
		  	throw new IllegalStateException(Constants.errorMessage);
		}

		public static class Component{
			private Component() {
			  	throw new IllegalStateException(Constants.errorMessage);
			}
	
			public static final int WIDTH_DEFAULT_BEGIN = 24;
			public static final int WIDTH_DEFAULT_MIDDLE = 4;
			public static final int WIDTH_DEFAULT_END = 16;
			public static final int HEIGHT_DEFAULT = 56;
			public static final int WIDTH_BEGIN = (int) (WIDTH_DEFAULT_BEGIN * App.SCALE);
			public static final int WIDTH_MIDDLE = (int) (WIDTH_DEFAULT_MIDDLE * App.SCALE);
			public static final int WIDTH_END = (int) (WIDTH_DEFAULT_END * App.SCALE);
			public static final int HEIGHT = (int) (HEIGHT_DEFAULT * App.SCALE);
		}
		
		public static class Buttons{
			private Buttons() {
			  	throw new IllegalStateException(Constants.errorMessage);
			}
	
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * App.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * App.SCALE);
		}
		public static class Inputs{
			private Inputs() {
			  	throw new IllegalStateException(Constants.errorMessage);
			}
	
			public static final int I_X_BOUNDS_POSITION = 10;
			public static final int I_Y_BOUNDS_POSITION = 32;
			public static final int I_FONT_SIZE_DEFAULT = 20;
			public static final int I_FONT_SIZE = (int) (I_FONT_SIZE_DEFAULT * App.SCALE);
			public static final int I_WIDTH_DEFAULT = 356;
			public static final int I_HEIGHT_DEFAULT = 56;
			public static final int I_WIDTH = (int) (I_WIDTH_DEFAULT * App.SCALE);
			public static final int I_HEIGHT = (int) (I_HEIGHT_DEFAULT * App.SCALE);
			public static final int LETTER_AND_NUMBER = 0;
			public static final int NUMBER_AND_POINT = 1;
			public static final int NUMBER_ONLY = 2;
		}
	}
	public static class Directions {
		private Directions() {
			  throw new IllegalStateException(Constants.errorMessage);
		}

		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	public static class PlayerConstants {
		private PlayerConstants() {
			  throw new IllegalStateException(Constants.errorMessage);
		}

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
		 * number of image per animation
		 * 
		 * @param playerAction for each action
		 * @return
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

	public static class PathConstants {
		private PathConstants() {
			  throw new IllegalStateException(Constants.errorMessage);
		}

		public static final String PROJECT_ROOT = System.getProperty("user.dir")+"/";
	}
}