package com.jbmo60927.communication.packets;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;
import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

import java.util.Objects;
import java.util.logging.Level;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;

public class Join extends Packet {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {
        "name", "version"
    });

    private final App app;

    public Join(Parameter[] parameters, App app) {
        super(Packet.TYPES.findType("Join"), parameters);
        this.app = app;
    }

    public Join(App app) {
        super(Packet.TYPES.findType("Join"), setParameters(app));
        this.app = app;
    }

    private static final Parameter[] setParameters(App app) {
        return new Parameter[] {
            new Parameter(Join.TYPES.findType("name"), stringToBytes("server")),
            new Parameter(Join.TYPES.findType("version"), stringToBytes(app.getVersion()))
        };
    }

    @Override
    public void execute() {
        String name = bytesToString(Packet.getParameterValue(Join.TYPES.findType("name"), parameters, stringToBytes("")));
        String version = bytesToString(Packet.getParameterValue(Join.TYPES.findType("version"), parameters, stringToBytes("")));

        if (!"".equals(version) && Objects.equals(version, app.getVersion()))
            LOGGER.log(Level.INFO, "versions are the same");
        else
            LOGGER.log(Level.INFO, () -> String.format("version are not the same (client:%s server:%s)", version, app.getVersion()));
            

    }
}
