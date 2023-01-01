package com.jbmo60927.command;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;

import java.util.Objects;

public class Quit extends Command {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {"message"});

    public Quit(Parameter[] parameters) {
        super(parameters);
    }

    @Override
    public String execute(App app) {
        app.quit();
        String stopReason = bytesToString(Parameter.hasParameter(parameters, Say.TYPES.findType("message")));
        if (Objects.equals("", stopReason))
            return "the serveur is stopping";
        return "the server is stopping\n" + stopReason;
    }
}