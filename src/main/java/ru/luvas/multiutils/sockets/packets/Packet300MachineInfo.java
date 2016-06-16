package ru.luvas.multiutils.sockets.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.RPacket;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@NoArgsConstructor
@AllArgsConstructor
public class Packet300MachineInfo extends RPacket {
    
    private String machine;
    private String ip;
    private int load;
    private int maxLoad;

    @Override
    public short getId() {
        return 300;
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeUTF(machine);
        dos.writeUTF(ip);
        dos.writeInt(load);
        dos.writeInt(maxLoad);
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        machine = dis.readUTF();
        ip = dis.readUTF();
        load = dis.readInt();
        maxLoad = dis.readInt();
    }

    @Override
    public void handleByClient() {
        try{
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            machine = addr.getHostName();
        }catch(UnknownHostException ex) {
            machine = "Unknown";
        }
        ip = RIndependentClient.getInstance().getIp();
        load = (int) ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        maxLoad = (int) ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        RIndependentClient.getInstance().send(this);
    }

    @Override
    public void handleByServer() {
        //Not here
    }

}
