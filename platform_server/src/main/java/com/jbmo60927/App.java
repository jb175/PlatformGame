package com.jbmo60927;

import java.io.IOException;

import com.jbmo60927.display.Game;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new Game();
    }
}
