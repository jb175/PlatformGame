package com.jbmo60927.command;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;
import com.jbmo60927.communication.packets.Welcome;

public class Sendwelcome extends Command {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {});

    public Sendwelcome(Parameter[] parameters) {
        super(parameters);
    }

    @Override
    public String execute(App app) {
        app.getConnect().getGameLinkThread().getSendPacket().sendPacket(new Welcome());
        return "welcome packet sent";
    }
}