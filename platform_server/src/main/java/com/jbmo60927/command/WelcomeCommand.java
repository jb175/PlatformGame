package com.jbmo60927.command;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;
import com.jbmo60927.communication.packets.Welcome;

public class WelcomeCommand extends Command {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {});

    public WelcomeCommand(Parameter[] parameters) {
        super(parameters);
    }

    @Override
    public String execute(App app) {
        app.getAcceptUserThread().broadcast(new Welcome());
        return "welcome packet sent";
    }
}