package ru.luvas.multiutils.sockets;

import java.util.concurrent.ExecutorService;
import lombok.Getter;
import ru.luvas.multiutils.achievements.Achievement;
import ru.luvas.multiutils.sockets.RExecutablePacket.Side;
import ru.luvas.multiutils.sockets.packets.*;

/**
 *
 * @author RinesThaix
 */
public class RSocketConnector {
    
    @Getter
    private static ConnectorMode connectorMode = null;
    
    @Getter
    private static Side side;
    
    @Getter
    private static ExecutorService executorService;
    
    public static void setConnectorMode(ConnectorMode mode, Side side, ExecutorService executor) {
        connectorMode = mode;
        RSocketConnector.side = side;
        executorService = executor;
        for(Achievement a : Achievement.values())
            a.getSection().getAchievements().add(a);
    }
    
    public static enum ConnectorMode {
        CLIENT,
        SERVER;
    }
    
    static {
        RPacketManager.register(Packet0Identifying.class);
        RPacketManager.register(Packet1Ping.class);
        RPacketManager.register(Packet2Achievements.class);
        RPacketManager.register(Packet3Friends.class);
        RPacketManager.register(Packet4FriendsUpdate.class);
        RPacketManager.register(Packet5Parties.class);
        RPacketManager.register(Packet6PartyUpdate.class);
        RPacketManager.register(Packet7Restart.class);
        RPacketManager.register(Packet8AchievementReplay.class);
        RPacketManager.register(Packet9NetworkLeveling.class);
    }
    
}
