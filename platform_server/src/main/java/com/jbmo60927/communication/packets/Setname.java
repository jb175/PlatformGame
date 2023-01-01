package com.jbmo60927.communication.packets;

import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;

public class Setname extends Packet {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {
        "name"
    });

    protected Setname(String name) {
        super(Packet.TYPES.findType("Setname"), new Parameter[] {new Parameter(Setname.TYPES.findType("name"), stringToBytes(name))});
    }

    @Override
    public void execute() {
    }
}
