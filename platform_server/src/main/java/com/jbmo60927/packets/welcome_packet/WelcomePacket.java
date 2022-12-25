package com.jbmo60927.packets.welcome_packet;

public class WelcomePacket {
    private WelcomePacket() {
        throw new IllegalStateException("Utility class");
    }

    public static final byte MESSAGE = 1;
    
	protected static final int[][] packetStructure = new int[][] {
        new int[] {MESSAGE, 100},
    };
}
