package ru.luvas.multiutils.sockets;

import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import lombok.Getter;
import ru.luvas.multiutils.Logger;
import ru.luvas.multiutils.sockets.RExecutablePacket.Side;
import ru.luvas.multiutils.sockets.packets.Packet1Ping;

/**
 *
 * @author RinesThaix
 */
public abstract class RIndependentClient extends RClient {
    
    private final Object locker = new Object();
    private final Random r = new Random();
    
    @Getter
    private static RIndependentClient instance;
    
    private final Map<String, RExecutablePacket> executables = new HashMap<>();
    private final Queue<RPacket> queue = new LinkedList<>();
    private long lastPacketAccepted = 0l;

    public RIndependentClient(final String serverIp, final int serverPort) {
        if(instance != null)
            throw new IllegalStateException("RIndependentClient is already created!");
        if(RSocketConnector.getConnectorMode() == null)
            throw new IllegalStateException("Connector mode is null! RIndependentClient couldn't be created!");
        if(RSocketConnector.getConnectorMode() == RSocketConnector.ConnectorMode.SERVER)
            throw new IllegalStateException("Connector mode is SERVER! RIndependentClient couldn't be created!");
        instance = this;
        Logger.log("New independent client created!");
        connect(serverIp, serverPort, false);
        RSocketConnector.getExecutorService().execute(new Runnable() {

            @Override
            public void run() {
                try {
                    while(!isDisconnected()) {
                        DataInputStream dis = getInputStream();
                        if(dis.available() > 0) {
                            RPacket packet = RPacketManager.createPacket(getInstance());
                            lastPacketAccepted = System.currentTimeMillis();
                            boolean executed = false;
                            if(packet.isExecutable()) {
                                RExecutablePacket execp = (RExecutablePacket) packet;
                                if(execp.getServerSide() == RSocketConnector.getSide() || execp.getServerSide() == Side.CLIENT) {
                                    RExecutablePacket trueExecp = executables.remove(execp.getUniqueId());
                                    if(trueExecp != null) {
                                        for(Field f : execp.getClass().getDeclaredFields()) {
                                            f.setAccessible(true);
                                            f.set(trueExecp, f.get(execp));
                                            f.setAccessible(false);
                                        }
                                        try {
                                            trueExecp.execute();
                                        }catch(Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        executed = true;
                                    }
                                }
                            }
                            if(!executed)
                                try {
                                    packet.handleByClient();
                                }catch(Exception ex) {
                                    ex.printStackTrace();
                                }
                        }
                        try {
                            Thread.sleep(50l);
                        }catch(InterruptedException ex) {}
                    }
                }catch(Exception ex) {
                    Logger.warn("Disabling due to unexpected network error!", ex);
                }finally {
                    restart();
                }
            }
            
        });
        RSocketConnector.getExecutorService().execute(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    if(!isDisconnected() && System.currentTimeMillis() - lastPacketAccepted > 5000l)
                        send(new Packet1Ping());
                    try {
                        Thread.sleep(10000l);
                    }catch(InterruptedException ex) {}
                }
            }
            
        });
    }
    
    private void connect(final String ip, final int port, boolean reconnect) {
        if(reconnect)
            try {
                Thread.sleep(5000l);
            }catch(InterruptedException ex) {}
        try {
            Socket socket = new Socket(ip, port);
            setSocket(socket);
            RSocketConnector.getExecutorService().execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        while(!isDisconnected()) {
                            boolean sleep = false;
                            synchronized(locker) {
                                if(!queue.isEmpty()) {
                                    RPacket packet = queue.peek();
                                    getOutputStream().writeShort(packet.getId());
                                    if(packet.isExecutable())
                                        getOutputStream().writeUTF(((RExecutablePacket) packet).getUniqueId());
                                    packet.write(getOutputStream());
                                    getOutputStream().flush();
                                    queue.poll();
                                }else
                                    sleep = true;
                            }
                            if(sleep)
                                try {
                                    Thread.sleep(50l);
                                }catch(InterruptedException ex) {}
                        }
                    }catch(SocketException ex) {
                        Logger.warn("Connection to the RServer disappeared! Reconnecting..");
                        connect(ip, port, true);
                    }catch(Exception ex) {
                        ex.printStackTrace();
                        disconnect();
                    }
                }

            });
            if(reconnect)
                Logger.log("Reconnected? Waiting for Multiproxy's response..");
        }catch(SocketException ex) {
            if(!reconnect)
                Logger.warn("Could not connect to the RServer! Reconnecting..");
            connect(ip, port, true);
        }catch(Exception ex) {
            Logger.warn("Could not connect to the RServer due to an unexpected cause!", ex);
        }
    }
    
    public abstract String getIp();
    
    public abstract int getPort();
    
    public abstract void updateServerName(String name);
    
    public abstract boolean isPlayerHere(String name);
    
    public abstract void restart();
    
    public void send(RPacket packet) {
        synchronized(locker) {
            if(queue.size() == 1000)
                return;
            queue.add(packet);
        }
    }
    
    public void send(RExecutablePacket packet, String uniqueId) {
        String uid = uniqueId + r.nextLong();
        packet.setUniqueId(uid);
        synchronized(locker) {
            if(queue.size() == 1000)
                return;
            queue.add(packet);
        }
        if(executables.containsKey(uid))
            throw new IllegalStateException("UniqueId '" + uid + "' is not unique for executable packet!");
        executables.put(uid, packet);
    }

}
