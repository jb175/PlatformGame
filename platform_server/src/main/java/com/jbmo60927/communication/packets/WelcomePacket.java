package com.jbmo60927.communication.packets;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;

public class WelcomePacket extends Packet {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {
        "message"
    });
    
    private static final String MESSAGE = "Welcome to the Platform Game. See more information at https://github.com/jb175/PlatformGame";

    public WelcomePacket(Parameter[] parameters) {
        super(PacketType.WELCOME, parameters);
    }

    public WelcomePacket()  {
        super(PacketType.WELCOME, setParameters());
    }

    private static final Parameter[] setParameters() {
        return new Parameter[] {
            new Parameter(WelcomePacket.TYPES.findType("message"), MESSAGE.getBytes(StandardCharsets.UTF_8))
        };
    }

    public void execute() {
        LOGGER.log(Level.INFO, "nothing to do with this packet");
    }

    public String getMessage() {
        return WelcomePacket.MESSAGE;
    }


	/**
	 * Packet type class
	 * every packet first byte contain the type of the packet
	 * this byte is composed from 4 bits for the type and 4 others for the sub-type
	 */
	public static final class ParameterType {
		private ParameterType() {
			throw new IllegalStateException("Utility class");
		}

        //null
		public static final byte NULL       = 0;
        //only parameter this packet will receive
		public static final byte MESSAGE    = 1;
	}
}
