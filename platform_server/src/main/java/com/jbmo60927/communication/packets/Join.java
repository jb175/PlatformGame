package com.jbmo60927.communication.packets;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;
import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

import java.util.Objects;
import java.util.logging.Level;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;
import com.jbmo60927.entities.ServerPlayer;

public class Join extends Packet {

    protected static final StringTypeList TYPES = new StringTypeList(new String[] {
        "name", "version"
    });

    private final App app;
    private int playerListNumber;

    public Join(Parameter[] parameters, App app, int playerListNumber) {
        super(Packet.TYPES.findType("Join"), parameters);
        this.app = app;
        this.playerListNumber = playerListNumber;
    }

    @Override
    public void execute() {
        String name = bytesToString(Packet.getParameterValue(Join.TYPES.findType("name"), parameters, stringToBytes("default")));
        String version = bytesToString(Packet.getParameterValue(Join.TYPES.findType("version"), parameters, stringToBytes("")));

        if (!"".equals(version) && Objects.equals(version, app.getVersion())) {
            String newName = name;
            int alreadyUsedName = 0;
            long millis = System.currentTimeMillis();
            while (System.currentTimeMillis() < millis+2000) {
                int c = 0;
                for (ServerPlayer player : app.getAcceptUserThread().getPlayers()) {
                    if(player != null && player.getName().equals(newName))
                        break;
                    c++;
                }
                if (c >= app.getAcceptUserThread().getPlayers().length)
                    break;
                else {
                    newName = String.format("%s(%d)", name, alreadyUsedName+2);
                }
            }
            if (System.currentTimeMillis() >= millis+2000) {
                LOGGER.log(Level.SEVERE, () -> "can't give a name to the player");
            }

            LOGGER.log(Level.INFO, "versions are the same");
            app.getAcceptUserThread().getPlayers()[playerListNumber].setName(newName);
            app.getAcceptUserThread().getPlayers()[playerListNumber].getServiceThread().getSendPacket().sendPacket(new Setname(newName));
        }
        else {
            LOGGER.log(Level.INFO, () -> String.format("version are not the same (client:%s server:%s)", version, app.getVersion()));
            app.getAcceptUserThread().getPlayers()[playerListNumber].getServiceThread().interrupt();
        }
    }
}
