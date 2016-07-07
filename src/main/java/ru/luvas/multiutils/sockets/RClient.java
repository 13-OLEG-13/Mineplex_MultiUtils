package ru.luvas.multiutils.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author RinesThaix
 */
@NoArgsConstructor
public class RClient {

    private final static String unk = "unknown";
    
    @Setter
    @Getter
    private String name = unk;
    
    @Getter
    private Socket socket;
    
    @Getter
    private DataInputStream inputStream;
    
    @Getter
    private DataOutputStream outputStream;
    
    @Getter
    private boolean disconnected = false;
    
    public RClient(Socket socket) {
        setSocket(socket);
    }
    
    protected void setSocket(Socket socket) {
        this.socket = socket;
        try {
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        }catch(Exception ex) {
            throw new IllegalStateException("Couldn't open input/output streams for RClient!", ex);
        }
    }
    
    public boolean isUnknown() {
        return name.equals(unk);
    }
    
    public void disconnect() {
        disconnect(false);
    }
    
    public synchronized void disconnect(boolean kicked) {
        if(disconnected)
            return;
        disconnected = true;
        try {
            socket.close();
        }catch(Exception ex) {}
        if(RSocketConnector.getConnectorMode() == RSocketConnector.ConnectorMode.SERVER)
            RServer.getInstance().disconnected(this, kicked);
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
