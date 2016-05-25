package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.luvas.multiutils.player.Friends;
import ru.luvas.multiutils.sockets.RPacket;

/**
 *
 * @author RinesThaix
 */
@NoArgsConstructor
@AllArgsConstructor
public class Packet4FriendsUpdate extends RPacket {
    
    @Getter
    private String player;
    
    @Getter
    private Action action;
    
    @Getter
    private String value;
    
    @Getter
    private long lastOnline;
    
    @Getter
    private String currentServer;

    @Override
    public short getId() {
        return 4;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(player);
        dos.writeUTF(action.name());
        dos.writeUTF(value);
        dos.writeLong(lastOnline);
        dos.writeUTF(currentServer);
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        player = dis.readUTF();
        action = Action.valueOf(dis.readUTF());
        value = dis.readUTF();
        lastOnline = dis.readLong();
        currentServer = dis.readUTF();
    }

    @Override
    public void handleByClient() {
        Friends f = Friends.getClearly(player);
        if(f == null)
            return;
        switch(action) {
            case ADDED_REQUEST:
                f.updateNewRequest(value);
                break;
            case REMOVED_REQUEST:
                f.updateOldRequest(value);
                break;
            case ADDED_FRIEND:
                f.updateNewFriend(value, lastOnline, currentServer);
                break;
            case REMOVED_FRIEND:
                f.updateOldFriend(value);
                break;
            case UPDATED:
                f.update(value, lastOnline, currentServer);
                break;
        }
    }

    @Override
    public void handleByServer() {
        //Nothing is here
    }
    
    public static enum Action {
        ADDED_REQUEST, REMOVED_REQUEST, ADDED_FRIEND, REMOVED_FRIEND, UPDATED;
    }

}
