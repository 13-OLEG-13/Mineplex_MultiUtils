package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.luvas.multiutils.player.PlayerDatas;
import ru.luvas.multiutils.player.sections.NetworkLeveling;
import ru.luvas.multiutils.sockets.RPacket;

/**
 *
 * @author RinesThaix
 */
@NoArgsConstructor
@AllArgsConstructor
public class Packet9NetworkLeveling extends RPacket {
    
    private String player;
    private int level;
    private int experience;

    @Override
    public short getId() {
        return 9;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(player);
        dos.writeInt(level);
        dos.writeInt(experience);
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        player = dis.readUTF();
        level = dis.readInt();
        experience = dis.readInt();
    }

    @Override
    public void handleByClient() {
        NetworkLeveling nl = PlayerDatas.getWithoutPreloading(NetworkLeveling.class, player);
        if(nl == null)
            return;
        if(level != 0)
            nl.updateLevel(level);
        if(experience != 0)
            nl.updateExperience(experience);
    }

    @Override
    public void handleByServer() {
        //Not here
    }

}
