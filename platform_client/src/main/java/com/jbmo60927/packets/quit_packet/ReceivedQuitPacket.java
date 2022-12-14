package com.jbmo60927.packets.quit_packet;

import java.io.IOException;
import java.util.logging.Level;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;

public class ReceivedQuitPacket extends ReceivedPacket implements QuitPacket {

    private App app;

    public ReceivedQuitPacket(App app) {
        super(PacketType.QUIT, new byte[] {}, "");
        this.app = app;
    }

    @Override
    public void execute() {
        try {
            app.getConnect().getSocketOfClient().close();
            app.getConnect().getGameLinkThread().interrupt();
            app.getConnect().setConnected(false);
            LOGGER.log(Level.INFO, "you leave the server");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "error during the closing of the connection with the server", e);
        }
    }
}
