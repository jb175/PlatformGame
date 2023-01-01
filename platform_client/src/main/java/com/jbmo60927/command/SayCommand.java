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

    public SayCommand(Parameter[] parameters) {
        super(parameters);
    }

    @Override
    public String execute(App app) {
        //if the message is empty
        if (Parameter.getParameter(parameters, SayCommand.TYPES.findType("message")) == new byte[] {})
            return "";
        app.getConnect().getGameLinkThread().getSendPacket().sendPacket(new Say(Packet.addParameter(parameters, new Parameter(Say.TYPES.findType("_from"), stringToBytes(app.getPlaying().getPlayer().getName())))));
        
        if (Parameter.getParameter(parameters, SayCommand.TYPES.findType("to")) == new byte[] {})
            return String.format("[%s] %s", app.getPlaying().getPlayer().getName(), bytesToString(Parameter.getParameter(parameters, SayCommand.TYPES.findType("message"))).replace('_', ' '));
        else
            return String.format("[%s] -> [%s] %s", app.getPlaying().getPlayer().getName(), bytesToString(Parameter.getParameter(parameters, SayCommand.TYPES.findType("to"))), bytesToString(Parameter.getParameter(parameters, SayCommand.TYPES.findType("message"))).replace('_', ' '));
    }
}

