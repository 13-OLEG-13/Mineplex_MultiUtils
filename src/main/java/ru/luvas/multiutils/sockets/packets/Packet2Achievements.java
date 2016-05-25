package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.luvas.multiutils.achievements.Achievements;
import ru.luvas.multiutils.sockets.RExecutablePacket;
import ru.luvas.multiutils.sockets.RServer;

/**
 *
 * @author RinesThaix
 */
@NoArgsConstructor
@AllArgsConstructor
public class Packet2Achievements extends RExecutablePacket {
    
    private String sender;
    private String player;
    private boolean getOrAdd;
    
    @Getter
    private String achievements;
    
    @Getter
    private int achievementId = 0;

    @Override
    public short getId() {
        return 2;
    }

    @Override
    public Side getServerSide() {
        return Side.CLIENT;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(sender);
        dos.writeUTF(player);
        dos.writeBoolean(getOrAdd);
        if(getOrAdd)
            dos.writeUTF(achievements);
        else
            dos.writeInt(achievementId);
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        sender = dis.readUTF();
        player = dis.readUTF();
        getOrAdd = dis.readBoolean();
        if(getOrAdd)
            achievements = dis.readUTF();
        else
            achievementId = dis.readInt();
    }

    @Override
    public void handleByClient() {
        //Nothing is here
    }

    @Override
    public void handleByServer() {
        Achievements achs = Achievements.get(player);
        if(getOrAdd) {
            achievements = achs.getAchievements();
            RServer.getInstance().send(this, sender);
        }else {
            if(achs.hasAchievement(achievementId)) {
                achievementId = -1;
                RServer.getInstance().send(this, sender);
                return;
            }
            achs.addAchievement(achievementId);
            RServer.getInstance().announce(this);
            if(!RServer.getInstance().getProxies().contains(sender))
                RServer.getInstance().send(this, sender);
        }
        //?
//        if(!Infractions.infractions.containsKey(player.toLowerCase()))
//            Achievements.invalidate(player);
    }

}
