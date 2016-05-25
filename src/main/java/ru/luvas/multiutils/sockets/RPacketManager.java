package ru.luvas.multiutils.sockets;

import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RinesThaix
 */
public class RPacketManager {

    private final static Map<Short, Class<? extends RPacket>> packets = new HashMap<>();
    private final static Map<Short, Byte> priorities = new HashMap<>();
    
    public static void register(Class<? extends RPacket> clazz) {
        register(clazz, (byte) 0);
    }
    
    public static void register(Class<? extends RPacket> clazz, byte priority) {
        try {
            short id = clazz.newInstance().getId();
            Byte b = priorities.get(id);
            if(b == null)
                b = -1;
            if(priority > b) {
                priorities.put(id, priority);
                packets.put(id, clazz);
            }
        }catch(Exception ex) {
            throw new IllegalStateException("Could not register packet named " + clazz.getName() + "!", ex);
        }
    }
    
    private static RPacket getEmptyPacket(short id) {
        try {
            Class<? extends RPacket> clazz = packets.get(id);
            if(clazz == null)
                throw new IllegalArgumentException("Unknown packet id: " + id);
            return clazz.newInstance();
        }catch(Exception ex) {
            throw new IllegalStateException("Could not create empty packet with id " + id + "!", ex);
        }
    }
    
    static RPacket createPacket(RClient client) {
        try {
            DataInputStream dis = client.getInputStream();
            short id = dis.readShort();
            RPacket packet = getEmptyPacket(id);
            if(packet.isExecutable())
                ((RExecutablePacket) packet).setUniqueId(dis.readUTF());
            packet.read(dis);
            packet.setClient(client);
            return packet;
        }catch(Exception ex) {
            throw new IllegalStateException("Could not create packet from the input stream!", ex);
        }
    }
    
}
