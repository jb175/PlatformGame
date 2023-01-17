package com.jbmo60927.utilz;

import com.jbmo60927.App;

public class Constants {
	private static String errorMessage = "Utility class";

	private Constants() {
	  	throw new IllegalStateException(Constants.errorMessage);
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

	public static class PathConstants {
		private PathConstants() {
			  throw new IllegalStateException(Constants.errorMessage);
		}

		public static final String PROJECT_ROOT = System.getProperty("user.dir")+"/";
	}
}