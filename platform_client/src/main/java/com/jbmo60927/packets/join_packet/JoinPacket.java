package com.jbmo60927.packets.join_packet;

public class JoinPacket {
    private JoinPacket() {
        throw new IllegalStateException("Utility class");
    }
    
	public static final int PLAYERNAMEBYTES = 20;
	public static final int PLAYERACTIONBYTES = 1;
}