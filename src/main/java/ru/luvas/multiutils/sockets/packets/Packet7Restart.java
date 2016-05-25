package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.RPacket;

/**
 *
 * @author RinesThaix
 */
public class Packet7Restart extends RPacket {

    @Override
    public short getId() {
        return 7;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        //Nothing is here
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        //Nothing is here
    }

    @Override
    public void handleByClient() {
        RIndependentClient.getInstance().restart();
    }

    @Override
    public void handleByServer() {
        //Nothing is here
    }

}
