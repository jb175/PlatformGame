package com.jbmo60927.communication.packets;

import static com.jbmo60927.utilz.HelpsMethods.bytesToString;
import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

import java.util.logging.Level;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;
import com.jbmo60927.entities.ServerPlayer;

public class Say extends Packet {

    public static final StringTypeList TYPES = new StringTypeList(new String[] {
        "message", "to", "_from"
    });

    private App app;
    private int playerListNumber;

    public Say(Parameter[] parameters, App app, int playerListNumber) {
        super(Packet.TYPES.findType("Say"), parameters);
        this.app = app;
        this.playerListNumber = playerListNumber;
    }

    @Override
    public void execute() {
        String message = bytesToString(Packet.getParameterValue(Say.TYPES.findType("message"), parameters, stringToBytes("")));
        String from = bytesToString(Packet.getParameterValue(Say.TYPES.findType("_from"), parameters, stringToBytes("")));
        String to = bytesToString(Packet.getParameterValue(Say.TYPES.findType("to"), parameters, stringToBytes("")));
        if (!message.equals("") && LOGGER.isLoggable(Level.INFO)) {
            if (to.equals("")) {
                this.app.getAcceptUserThread().getPlayers()[this.playerListNumber].getServiceThread().broadcast(this);
                LOGGER.log(Level.INFO, String.format("[%s] %s", from, message.replace('_', ' ')));
            } else {
                for (ServerPlayer player : app.getAcceptUserThread().getPlayers()) {
                    if (player.getName().equals(to)) {
                        player.getServiceThread().getSendPacket().sendPacket(this);
                        break;
                    }
                }
                LOGGER.log(Level.INFO, String.format("[%s] -> [%s] %s", from, to, message.replace('_', ' ')));
            }
        }
    }
}
