package com.jbmo60927.communication.packets;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;
import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;

public class Welcome extends Packet {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {
        "message"
    });
    
    private static final String MESSAGE = "Welcome to the Platform Game. See more information at https://github.com/jb175/PlatformGame";

    public Welcome(Parameter[] parameters, App app) {
        super(Packet.TYPES.findType("Welcome"), parameters);
    }

    public Welcome()  {
        super(Packet.TYPES.findType("Welcome"), setParameters());
    }

    private static final Parameter[] setParameters() {
        return new Parameter[] {
            new Parameter(Welcome.TYPES.findType("message"), MESSAGE.getBytes(StandardCharsets.UTF_8))
        };
    }

    @Override
    public void execute() {
        String message = bytesToString(Packet.getParameterValue(Welcome.TYPES.findType("message"), parameters, stringToBytes("nothing to show")));
        LOGGER.log(Level.INFO, message);
    }
}
