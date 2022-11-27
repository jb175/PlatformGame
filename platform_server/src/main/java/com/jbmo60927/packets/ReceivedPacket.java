package com.jbmo60927.packets;

public abstract class ReceivedPacket extends Packet {

    protected String message = "";

    protected ReceivedPacket(int packetType, byte[] parameters) {
        super(packetType, parameters.length, parameters);
    }

    public String getMessage() {
        return message;
    }

    //when the data is received what we need to do with it
    public abstract void execute();
}
