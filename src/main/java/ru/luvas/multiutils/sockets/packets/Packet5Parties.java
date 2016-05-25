package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.luvas.multiutils.player.Party.PartyMember;
import ru.luvas.multiutils.sockets.RExecutablePacket;

/**
 *
 * @author RinesThaix
 */
@SuppressWarnings("unchecked")
@NoArgsConstructor
@AllArgsConstructor
public class Packet5Parties extends RExecutablePacket {
    
    @Getter
    private String player;
    
    @Getter
    private Action action;
    
    @Getter
    private String argument;
    
    @Getter
    private List<PartyMember> members;

    @Override
    public Side getServerSide() {
        return Side.CLIENT;
    }

    @Override
    public short getId() {
        return 5;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(player);
        dos.writeUTF(action.name());
        dos.writeUTF(argument);
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        player = dis.readUTF();
        int size = dis.readInt();
        members = new ArrayList(size);
        for(int i = 0; i < size; ++i)
            members.add(new PartyMember(dis.readUTF(), dis.readUTF(), dis.readUTF()));
    }

    @Override
    public void handleByClient() {
        //Nothing is here
    }

    @Override
    public void handleByServer() {
        //Not here
    }
    
    public static enum Action {
        GET, CREATE, INVITE, DISBAND, LEAVE, KICK, ACCEPT, DENY, CHANGE_LEADER, SEND_MESSAGE;
    }

}
