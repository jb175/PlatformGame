package com.jbmo60927.gamestates;

public enum GameStates {
    PLAYING, MENU, CONNECT, OPTIONS, QUIT;

    private static GameStates state = MENU;

    public static void setGameState(GameStates gameState) {
        state = gameState;
    }

    public static GameStates getGameState() {
        return state;
    }
}
