package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.RPacket;
import ru.luvas.multiutils.sockets.RServer;
import ru.luvas.multiutils.sockets.RSocketConnector;

/**
 *
 * @author RinesThaix
 */
@NoArgsConstructor
@AllArgsConstructor
public class Packet0Identifying extends RPacket {
    
    private String var1;
    private int var2;

    @Override
    public short getId() {
        return 0;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        if(RSocketConnector.getConnectorMode() == RSocketConnector.ConnectorMode.CLIENT) {
            RIndependentClient independent = RIndependentClient.getInstance();
            dos.writeUTF(independent.getIp());
            dos.writeInt(independent.getPort());
        }else {
            dos.writeUTF(var1 == null ? "null" : var1);
            dos.writeInt(0);
        }
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        var1 = dis.readUTF();
        var2 = dis.readInt();
    }

    @Override
    public void handleByClient() {
        if(var1.equals("null"))
            RIndependentClient.getInstance().send(this);
        else {
            RIndependentClient.getInstance().updateServerName(var1);
            RIndependentClient.getInstance().setName(var1);
        }
    }

    @Override
    public void handleByServer() {
        RServer.getInstance().identify(getClient(), var1, var2);
    }

}
