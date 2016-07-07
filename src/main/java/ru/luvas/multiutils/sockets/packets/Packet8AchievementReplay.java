package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.luvas.multiutils.achievements.Achievement;
import ru.luvas.multiutils.achievements.Achievements;
import ru.luvas.multiutils.player.PlayerDatas;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.RPacket;
import ru.luvas.multiutils.sockets.RServer;

/**
 *
 * @author RinesThaix
 */
@NoArgsConstructor
@AllArgsConstructor
public class Packet8AchievementReplay extends RPacket {
    
    private String player;
    private int id;

    @Override
    public short getId() {
        return 8;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(player);
        dos.writeInt(id);
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        player = dis.readUTF();
        id = dis.readInt();
    }

    @Override
    public void handleByClient() {
        if(!RIndependentClient.getInstance().isPlayerHere(player))
            return;
        Achievements.runnable.got(player, Achievement.getById(id));
        PlayerDatas.get(Achievements.class, player).updateAchievement(id);
    }

    @Override
    public void handleByServer() {
        String server = RServer.getInstance().getServer(player);
        if(server != null)
            RServer.getInstance().send(this, server);
    }

}
