package com.jbmo60927.gamestates;

public enum GameStates {
    MENU, DRAWING, QUIT;

    private static GameStates state = MENU;

    public static void setGameState(GameStates gameState) {
        state = gameState;
    }

    public static GameStates getGameState() {
        return state;
    }
}
