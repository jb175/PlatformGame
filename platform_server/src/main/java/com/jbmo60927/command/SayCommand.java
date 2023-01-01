package com.jbmo60927.command;

import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;

import com.jbmo60927.App;

public class SayCommand extends Command {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {"message"});

    public SayCommand(Parameter[] parameters) {
        super(parameters);
    }

    @Override
    public String execute(App app) {
        return bytesToString(Parameter.hasParameter(parameters, SayCommand.TYPES.findType("message"))).replace('_', ' ');
    }
}
