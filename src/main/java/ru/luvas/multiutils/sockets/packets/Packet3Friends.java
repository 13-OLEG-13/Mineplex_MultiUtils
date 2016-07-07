package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.luvas.multiutils.player.sections.Friends.FriendInfo;
import ru.luvas.multiutils.sockets.RExecutablePacket;

/**
 *
 * @author RinesThaix
 */
@SuppressWarnings("unchecked")
@NoArgsConstructor
public class Packet3Friends extends RExecutablePacket {
    
    @Getter
    private String player;
    
    @Getter
    private Action action;
    
    @Getter
    private String argument;
    
    @Getter
    private List<FriendInfo> friends;
    
    @Getter
    private List<String> friendRequests;
    
    @Getter
    private String extra;
    
    public Packet3Friends(String player, Action action, String argument, String extra) {
        this.player = player;
        this.action = action;
        this.argument = argument;
        this.extra = extra;
    }

    @Override
    public Side getServerSide() {
        return Side.CLIENT;
    }

    @Override
    public short getId() {
        return 3;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(player);
        dos.writeUTF(action.name());
        dos.writeUTF(argument);
        dos.writeUTF(extra);
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        player = dis.readUTF();
        int size = dis.readInt();
        friends = new ArrayList(size);
        for(int i = 0; i < size; ++i)
            friends.add(new FriendInfo(dis.readUTF(), dis.readBoolean(), dis.readLong(), dis.readUTF()));
        size = dis.readInt();
        friendRequests = new ArrayList(size);
        for(int i = 0; i < size; ++i)
            friendRequests.add(dis.readUTF());
        extra = dis.readUTF();
    }

    @Override
    public void handleByClient() {
        //Nothing here
    }

    @Override
    public void handleByServer() {
        //Not here
    }
    
    public static enum Action {
        GET, ADD, REMOVE;
    }

}
