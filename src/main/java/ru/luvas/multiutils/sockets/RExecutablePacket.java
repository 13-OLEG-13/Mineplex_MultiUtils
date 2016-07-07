package ru.luvas.multiutils.sockets;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author RinesThaix
 */
public abstract class RExecutablePacket extends RPacket {
    
    @Setter
    @Getter
    private String uniqueId;
    
    public abstract Side getServerSide();
    
    public void execute() {
        throw new UnsupportedOperationException("Execute method is empty!");
    }

    @Override
    public final boolean isExecutable() {
        return true;
    }
    
    public static enum Side {
        SPIGOT, PROXY, STATISTICS, CLIENT, MULTIPROXY;
    }
    
}
