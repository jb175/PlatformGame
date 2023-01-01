package com.jbmo60927.communication.packets;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;
import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

import java.util.logging.Level;

import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;

public class Say extends Packet {

    public static final StringTypeList TYPES = new StringTypeList(new String[] {
        "message", "to", "_from"
    });

    public Say(Parameter[] parameters) {
        super(Packet.TYPES.findType("Say"), parameters);
    }

    @Override
    public void execute() {
        String message = bytesToString(Packet.getParameterValue(Say.TYPES.findType("message"), parameters, stringToBytes("")));
        String from = bytesToString(Packet.getParameterValue(Say.TYPES.findType("_from"), parameters, stringToBytes("")));
        String to = bytesToString(Packet.getParameterValue(Say.TYPES.findType("to"), parameters, stringToBytes("")));
        if (!message.equals("") && LOGGER.isLoggable(Level.INFO)) {
            if (to.equals(""))
                LOGGER.log(Level.INFO, String.format("[%s] %s", from, message.replace('_', ' ')));
            else
                LOGGER.log(Level.INFO, String.format("[%s] -> [%s] %s", from, to, message.replace('_', ' ')));
        }
    }
}
