package com.jbmo60927.packets.quit_packet;

import java.nio.charset.StandardCharsets;

import com.jbmo60927.App;
import com.jbmo60927.packets.ReceivedPacket;
import com.jbmo60927.thread.ServiceThread;

public class ReceivedQuitPacket extends ReceivedPacket implements QuitPacket {

    App app;
    ServiceThread serviceThread;

    public ReceivedQuitPacket(byte[] parameters, App app, ServiceThread serviceThread) {
        super(PacketType.QUIT, new byte[] {}, String.format("%s left the game", new String(parameters, StandardCharsets.UTF_8)));
        this.app = app;
        this.serviceThread = serviceThread;
    }

    @Override
    public void execute() {
        serviceThread.sendPacket(new SendQuitPacket());
        serviceThread.interrupt();
    }
}
