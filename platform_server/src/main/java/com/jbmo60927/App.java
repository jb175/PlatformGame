package com.jbmo60927;

import java.io.IOException;

import com.jbmo60927.display.Game;
import com.jbmo60927.logger.MyLogger;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     * @throws IOException when the logger can't be created
     */
    public static void main(final String[] args) throws IOException {
        MyLogger.setup();
        new Game();
    }
}
