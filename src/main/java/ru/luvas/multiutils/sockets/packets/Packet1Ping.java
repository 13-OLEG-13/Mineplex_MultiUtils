package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.NoArgsConstructor;
import ru.luvas.multiutils.sockets.RPacket;
import ru.luvas.multiutils.sockets.RServer;

/**
 *
 * @author RinesThaix
 */
@NoArgsConstructor
public class Packet1Ping extends RPacket {

    @Override
    public short getId() {
        return 1;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        //Nothing
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        //Nothing
    }

    @Override
    public void handleByClient() {
        //Nothing
    }

    @Override
    public void handleByServer() {
        RServer.getInstance().send(this, getClient());
    }

}
