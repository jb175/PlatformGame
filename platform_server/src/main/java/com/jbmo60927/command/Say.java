package com.jbmo60927.command;

import com.jbmo60927.communication.TypeList;
import com.jbmo60927.communication.parameters.Parameter;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;

public class Say extends Command {

    protected static final TypeList TYPES = new TypeList(new String[] {"message"});

    public Say(Parameter[] parameters) {
        super(parameters);
    }

    @Override
    public String execute() {
        return bytesToString(Parameter.hasParameter(parameters, TYPES.findValue("message")));
    }
}
