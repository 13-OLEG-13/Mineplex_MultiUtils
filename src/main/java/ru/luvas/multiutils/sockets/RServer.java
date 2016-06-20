package ru.luvas.multiutils.sockets;

import ru.luvas.multiutils.sockets.packets.Packet0Identifying;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.Getter;
import ru.luvas.multiutils.Logger;
import ru.luvas.multiutils.sockets.RExecutablePacket.Side;

/**
 *
 * @author RinesThaix
 */
public abstract class RServer {
    
    private final Object locker = new Object();
    private final Random r = new Random();
    private static RServer instance;
    
    private ServerSocket server;
    private boolean interrupted = false;
    private final ReadWriteLock clientsLock = new ReentrantReadWriteLock();
    private final Map<String, RClient> clients = new HashMap<>();
    private final Map<String, RExecutablePacket> executables = new HashMap<>();
    
    @Getter
    private final Queue<QueuedPacket> queue = new LinkedList<>();

    public RServer(int port) {
        if(instance != null)
            throw new IllegalStateException("RServer is already created!");
        if(RSocketConnector.getConnectorMode() == null)
            throw new IllegalStateException("Connector mode is null! RServer couldn't be created!");
        if(RSocketConnector.getConnectorMode() == RSocketConnector.ConnectorMode.CLIENT)
            throw new IllegalStateException("Connector mode is CLIENT! RServer couldn't be created!");
        instance = this;
        try {
            server = new ServerSocket(port);
            RSocketConnector.getExecutorService().execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        while(!interrupted)
                            handle(server.accept());
                    }catch(Exception ex) {
                        if(!(ex instanceof SocketException))
                            ex.printStackTrace();
                    }
                }
                
            });
            RSocketConnector.getExecutorService().execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        while(!interrupted) {
                            boolean sleep = false;
                            QueuedPacket qpacket = null;
                            synchronized(locker) {
                                if(!queue.isEmpty())
                                    qpacket = queue.poll();
                                else
                                    sleep = true;
                            }
                            if(!sleep) {
                                RPacket packet = qpacket.packet;
                                RClient receiver = qpacket.receiver;
                                if(receiver.isDisconnected())
                                    continue;
                                Future<?> future = RSocketConnector.getExecutorService().submit(() -> {
                                    try {
                                        DataOutputStream dos = receiver.getOutputStream();
                                        dos.writeShort(packet.getId());
                                        if(packet.isExecutable())
                                            dos.writeUTF(((RExecutablePacket) packet).getUniqueId());
                                        packet.write(dos);
                                        dos.flush();
                                    }catch(SocketException ex) {
                                        receiver.disconnect();
                                        //Client disappeared
                                    }catch(Exception ex) {
                                        receiver.disconnect();
                                        ex.printStackTrace();
                                    }
                                });
                                try {
                                    future.get(1, TimeUnit.SECONDS);
                                }catch(Exception ex) {}
                            }
                            if(sleep)
                                try {
                                    Thread.sleep(50l);
                                }catch(InterruptedException ex) {}
                        }
                    }catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
                
            });
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public Collection<RClient> getClients() {
        clientsLock.readLock().lock();
        try {
            return new HashSet<>(clients.values());
        }finally {
            clientsLock.readLock().unlock();
        }
    }
    
    public abstract String getServerName(RClient client, String ip, int port);
    
    public abstract void onDisconnect(String clientName);
    
    public abstract Collection<String> getProxies();
    
    public abstract String getProxy(String player);
    
    public abstract String getServer(String player);
    
    public void identify(RClient client, String ip, int port) {
        String name = getServerName(client, ip, port);
        if(name == null)
            throw new IllegalStateException("Could not identify server " + ip + ":" + port + "!");
        client.setName(name);
        clientsLock.writeLock().lock();
        try {
            if(clients.containsKey(name))
                clients.get(name).disconnect();
            clients.put(name, client);
        }finally {
            clientsLock.writeLock().unlock();
        }
        send(new Packet0Identifying(name, 0), client);
    }
    
    void disconnected(RClient client) {
        disconnected(client, false);
    }
    
    void disconnected(RClient client, boolean kicked) {
        if(!client.isUnknown()) {
            if(kicked)
                clients.remove(client.getName());
            else {
                clientsLock.writeLock().lock();
                try {
                    clients.remove(client.getName());
                }finally {
                    clientsLock.writeLock().unlock();
                }
            }
            onDisconnect(client.getName());
        }
    }
    
    public void interrupt() {
        interrupted = true;
        try {
            server.close();
        }catch(Exception ex) {}
    }
    
    private long lastWarning = 0l;
    
    private boolean checkPacketsOverflow() {
        if(queue.size() >= 2500) {
            long current = System.currentTimeMillis();
            if(current - lastWarning > 60000l) {
                Logger.warn("Can not handle so many packets! There's a packets overflow!");
                lastWarning = current;
            }
            return true;
        }
        return false;
    }
    
    public void send(RPacket packet, RClient client) {
        packet.setClient(client);
        synchronized(locker) {
            if(checkPacketsOverflow())
                return;
            queue.add(new QueuedPacket(client, packet));
        }
    }
    
    public void send(RPacket packet, String clientName) {
        RClient client;
        clientsLock.readLock().lock();
        try {
            client = clients.get(clientName);
        }finally {
            clientsLock.readLock().unlock();
        }
        if(client == null)
            throw new IllegalArgumentException("Could not find RClient named " + clientName + "!");
        send(packet, client);
    }
    
    public void send(RExecutablePacket packet, String uniqueId, RClient client) {
        String uid = uniqueId + r.nextLong();
        packet.setUniqueId(uid);
        packet.setClient(client);
        synchronized(locker) {
            if(checkPacketsOverflow())
                return;
            queue.add(new QueuedPacket(client, packet));
        }
        if(executables.containsKey(uid))
            throw new IllegalStateException("UniqueId '" + uid + "' is not unique for executable packet!");
        executables.put(uid, packet);
    }
    
    public void send(RExecutablePacket packet, String uniqueId, String clientName) {
        RClient client;
        clientsLock.readLock().lock();
        try {
            client = clients.get(clientName);
        }finally {
            clientsLock.readLock().unlock();
        }
        if(client == null)
            throw new IllegalArgumentException("Could not find RClient named " + clientName + "!");
        send(packet, uniqueId, client);
    }
    
    public RClient getClient(String clientName) {
        clientsLock.readLock().lock();
        try {
            return clients.get(clientName);
        }finally {
            clientsLock.readLock().unlock();
        }
    }
    
    public void announce(RPacket packet) {
        for(String proxy : getProxies())
            send(packet, proxy);
    }
    
    private void handle(Socket client) {
        final RClient rclient = new RClient(client);
        RSocketConnector.getExecutorService().execute(new Runnable() {

            @Override
            public void run() {
                try {
                    while(!rclient.isDisconnected()) {
                        DataInputStream dis = rclient.getInputStream();
                        if(dis.available() > 0) {
                            RPacket packet = RPacketManager.createPacket(rclient);
                            boolean executed = false;
                            if(packet.isExecutable()) {
                                RExecutablePacket execp = (RExecutablePacket) packet;
                                if(execp.getServerSide() == Side.MULTIPROXY) {
                                    executed = true;
                                    RExecutablePacket trueExecp = executables.remove(execp.getUniqueId());
                                    for(Field f : execp.getClass().getDeclaredFields()) {
                                        f.setAccessible(true);
                                        f.set(trueExecp, f.get(execp));
                                        f.setAccessible(false);
                                    }
                                    try {
                                        trueExecp.execute();
                                    }catch(Exception ex) {
                                        Logger.warn("Can not execute packet!", ex);
                                    }
                                }
                            }
                            if(!executed)
                                try {
                                    packet.handleByServer();
                                }catch(Exception ex) {
                                    Logger.warn("Can not handle packet by server!", ex);
                                }
                        }
                        try {
                            Thread.sleep(50l);
                        }catch(InterruptedException ex) {}
                    }
                }catch(Exception ex) {
                    ex.printStackTrace();
                }finally {
                    rclient.disconnect();
                }
            }
            
        });
        send(new Packet0Identifying(), rclient);
    }
    
    public static RServer getInstance() {
        return instance;
    }
    
    private static class QueuedPacket {
        
        private final RClient receiver;
        private final RPacket packet;
        
        public QueuedPacket(RClient receiver, RPacket packet) {
            this.receiver = receiver;
            this.packet = packet;
        }
        
        @Override
        public String toString() {
            return "{" + receiver.getName() + ";" + packet.getId() + "}";
        }
        
    }
    
}
