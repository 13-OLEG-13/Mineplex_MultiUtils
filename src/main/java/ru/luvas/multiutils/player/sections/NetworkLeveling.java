package ru.luvas.multiutils.player.sections;

import lombok.Getter;
import ru.luvas.multiutils.player.Section;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.RSocketConnector;
import ru.luvas.multiutils.sockets.packets.Packet9NetworkLeveling;

/**
 *
 * @author RinesThaix
 */
public class NetworkLeveling extends Section {
    
    @Getter
    private int level;
    
    @Getter
    private int experience;
    
    public NetworkLeveling(String owner) {
        super(owner);
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
    
    private final static int[] exp_needed = new int[111];
    
    static {
        exp_needed[0] = 5000;
        for(int i = 1; i < exp_needed.length; ++i)
            exp_needed[i] = exp_needed[i - 1] + 1250;
    }
    
}
