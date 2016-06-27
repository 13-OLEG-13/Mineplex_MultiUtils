package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.luvas.multiutils.player.Party;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.RPacket;

/**
 *
 * @author RinesThaix
 */
@NoArgsConstructor
@AllArgsConstructor
public class Packet6PartyUpdate extends RPacket {
    
    @Getter
    private String player;
    
    @Getter
    private Action action;
    
    @Getter
    private String argument;
    
    @Getter
    private String coloredName;
    
    @Getter
    private String server;

    @Override
    public short getId() {
        return 6;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(player);
        dos.writeUTF(action.name());
        dos.writeUTF(argument);
        dos.writeUTF(coloredName);
        dos.writeUTF(server);
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        player = dis.readUTF();
        action = Action.valueOf(dis.readUTF());
        argument = dis.readUTF();
        coloredName = dis.readUTF();
        server = dis.readUTF();
    }

    @Override
    public void handleByClient() {
        Party p = Party.get(player);
        switch(action) {
            case NEW_MEMBER: {
                if(p != null)
                    p.updateNewMember(argument, coloredName, server);
                else if(RIndependentClient.getInstance().isPlayerHere(argument)) {
                    RIndependentClient.getInstance().send(new Packet5Parties(player, Packet5Parties.Action.GET, "null", null) {
                    
                        @Override
                        public void execute() {
                            if(getMembers().isEmpty())
                                return;
                            Party.updateRecreation(getMembers());
                        }
                    
                    }, argument);
                }
                break;
            }case REMOVED_MEMBER: {
                if(p == null)
                    break;
                p.updateOldMember(argument);
                Party.getParties().remove(argument.toLowerCase());
                break;
            }case CHANGED_LEADER: {
                if(p == null)
                    break;
                p.updateLeader(argument);
                break;
            }case DISBANDED: {
                if(p == null)
                    break;
                p.updateDisbanded();
                break;
            }case SERVER_CHANGE: {
                if(p == null)
                    break;
                p.updateServer(argument, server);
                break;
            }case CREATED: {
                Party.updateCreation(player, coloredName, server);
                break;
            }
        }
    }

    @Override
    public void handleByServer() {
        //Nothing is here
    }
    
    public static enum Action {
        NEW_MEMBER, REMOVED_MEMBER, CHANGED_LEADER, DISBANDED, SERVER_CHANGE, CREATED;
    }

}
