package com.jbmo60927.communication.packets;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;

public class Setname extends Packet {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {
        "name"
    });

    private App app;

    protected Setname(Parameter[] parameters, App app) {
        super(Packet.TYPES.findType("Setname"), parameters);
        this.app = app;
    }

    @Override
    public void execute() {
        String name = bytesToString(Parameter.getParameter(parameters, Setname.TYPES.findType("name")));
        app.getPlaying().getPlayer().setName(name);
    }
}
