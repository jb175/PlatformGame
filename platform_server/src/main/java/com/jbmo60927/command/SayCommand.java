package com.jbmo60927.command;

import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.packets.Packet;
import com.jbmo60927.communication.packets.Say;
import com.jbmo60927.communication.types.StringTypeList;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;
import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

import com.jbmo60927.App;

public class SayCommand extends Command {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {"message", "to"});
    private static final String SERVERNAME = "Server";

    public SayCommand(Parameter[] parameters) {
        super(parameters);
    }

    @Override
    public String execute(App app) {
        //if the message is empty
        if (Parameter.getParameter(parameters, SayCommand.TYPES.findType("message")) == new byte[] {})
            return "";
        app.getAcceptUserThread().broadcast(new Say(Packet.addParameter(parameters, new Parameter(Say.TYPES.findType("_from"), stringToBytes(SayCommand.SERVERNAME))), app, -1));
        return String.format("[%s] %s", SayCommand.SERVERNAME, bytesToString(Parameter.getParameter(parameters, SayCommand.TYPES.findType("message"))).replace('_', ' '));
    }
}
