package com.jbmo60927.communication.packets;

import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;

public class Join extends Packet {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {
        "name", "version"
    });

    private final App app;

    public Join(App app) {
        super(Packet.TYPES.findType("Join"), setParameters(app));
        this.app = app;
    }

    private static final Parameter[] setParameters(App app) {
        return new Parameter[] {
            new Parameter(Join.TYPES.findType("name"), stringToBytes(app.getPlaying().getPlayer().getName())),
            new Parameter(Join.TYPES.findType("version"), stringToBytes(App.VERSION))
        };
    }

    @Override
    public void execute() {
    }
}
