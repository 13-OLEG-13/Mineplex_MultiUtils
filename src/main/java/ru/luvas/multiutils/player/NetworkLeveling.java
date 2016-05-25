package ru.luvas.multiutils.player;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.RSocketConnector;
import ru.luvas.multiutils.sockets.packets.Packet9NetworkLeveling;

/**
 *
 * @author RinesThaix
 */
public class NetworkLeveling {

    private final static Map<String, NetworkLeveling> nls = new HashMap<>();
    
    public static NetworkLeveling getClearly(String owner) {
        return nls.get(owner.toLowerCase());
    }
    
    public static NetworkLeveling get(String owner) {
        NetworkLeveling nl = nls.get(owner.toLowerCase());
        if(nl != null)
            return nl;
        nl = new NetworkLeveling(owner);
        nls.put(owner.toLowerCase(), nl);
        return nl;
    }
    
    public static void invalidate(String owner) {
        nls.remove(owner.toLowerCase());
    }
    
    @Getter
    private final String owner;
    
    @Getter
    private int level;
    
    @Getter
    private int experience;
    
    public NetworkLeveling(String owner) {
        this.owner = owner;
        if(RSocketConnector.getConnectorMode() == RSocketConnector.ConnectorMode.CLIENT) {
            RIndependentClient.getInstance().send(new Packet9NetworkLeveling(owner, 0, 0));
        }else
            throw new IllegalStateException("This class mustn't be used by the multiproxy!");
    }
    
    public void addExperience(int value) {
        if(value <= 0)
            return;
        RIndependentClient.getInstance().send(new Packet9NetworkLeveling(owner, 0, value));
    }
    
    public void updateLevel(int value) {
        this.level = value;
    }
    
    public void updateExperience(int value) {
        this.experience = value;
    }
    
    public static int getNextLevelNeeded(int currentLevel) {
        return exp_needed[currentLevel - 1];
    }
    
    private final static int[] exp_needed = new int[100];
    
    static {
        exp_needed[0] = 1000;
        for(int i = 1; i < exp_needed.length; ++i)
            exp_needed[i] = (int) (exp_needed[i - 1] * 1.1d);
    }
    
}
