package ru.luvas.multiutils.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author RinesThaix
 */
public abstract class RPacket {
    
    private RClient client;
    
    protected void setClient(RClient client) {
        this.client = client;
    }
    
    public RClient getClient() {
        return client;
    }
    
    public boolean isExecutable() {
        return false;
    }

    public abstract short getId();
    
    public abstract void write(DataOutputStream dos) throws IOException;
    
    public abstract void read(DataInputStream dis) throws IOException;
    
    public abstract void handleByClient();
    
    public abstract void handleByServer();
    
}
