package ru.luvas.multiutils.achievements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.luvas.multiutils.MultiUtils;
import ru.luvas.multiutils.Storager;
import ru.luvas.multiutils.player.Infractions;
import ru.luvas.multiutils.sockets.RExecutablePacket.Side;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.RSocketConnector;
import ru.luvas.multiutils.sockets.RSocketConnector.ConnectorMode;
import ru.luvas.multiutils.sockets.packets.Packet2Achievements;

/**
 *
 * @author RinesThaix
 */
public class Achievements {

    private final static Map<String, Achievements> achs = new HashMap<>();
    public static GotAchievementRunnable runnable = new GotAchievementRunnable();
    
    public static Achievements get(String owner) {
        Achievements ach = achs.get(owner.toLowerCase());
        if(ach != null)
            return ach;
        ach = new Achievements(owner);
        achs.put(owner.toLowerCase(), ach);
        return ach;
    }
    
    public static void invalidate(String owner) {
        achs.remove(owner.toLowerCase());
    }
    
    @Getter
    private final String owner;
    
    @Setter
    @Getter
    private String achievements;
    
    public Achievements(String owner) {
        this.owner = owner;
        if(RSocketConnector.getSide() == Side.SPIGOT) {
            RIndependentClient.getInstance().send(new Packet2Achievements(RIndependentClient.getInstance().getName(), owner, true, "null", 0) {
            
                @Override
                public void execute() {
                    achievements = getAchievements();
                }
            
            }, owner);
        }else {
            try(ResultSet set = MultiUtils.getProxyConnector().query("SELECT achievements FROM achievements WHERE player_name='" + owner + "'")) {
                if(set.next())
                    achievements = set.getString("achievements");
                else {
                    achievements = Storager.getDefault(99);
                    MultiUtils.getProxyConnector().addToQueue("INSERT INTO achievements VALUES ('%s', '%s')",
                            owner, achievements);
                }
            }catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public boolean hasAchievement(Achievement a) {
        return hasAchievement(a.getId());
    }
    
    public boolean hasAchievement(int id) {
        return Storager.has(achievements, id);
    }
    
    public void updateAchievement(int id) {
        achievements = Storager.update(achievements, id, true);
    }
    
    public void addAchievement(final Achievement a) {
        if(RSocketConnector.getConnectorMode() != ConnectorMode.CLIENT)
            throw new IllegalStateException("This method can't be used by multiproxy!");
        if(achievements == null || hasAchievement(a))
            return;
        RIndependentClient.getInstance().send(new Packet2Achievements(RIndependentClient.getInstance().getName(), owner, false, "null", a.getId()) {
        
            @Override
            public void execute() {
                int id = getAchievementId();
                if(id == -1)
                    return;
                achievements = Storager.update(achievements, id, true);
                runnable.got(owner, a);
                int total = Storager.countTotal(achievements);
                switch(total) {
                    case 10:
                        addAchievement(Achievement.FOS_KEEPER_I);
                        break;
                    case 25:
                        addAchievement(Achievement.FOS_KEEPER_II);
                        break;
                    case 50:
                        addAchievement(Achievement.FOS_KEEPER_III);
                        break;
                    case 75:
                        addAchievement(Achievement.FOS_KEEPER_IV);
                        break;
                    case 100:
                        addAchievement(Achievement.FOS_KEEPER_V);
                        break;
                }
            }
        
        }, owner);
    }
    
    public void addAchievement(int id) {
        if(RSocketConnector.getConnectorMode() != ConnectorMode.SERVER)
            throw new IllegalStateException("This method could only be called by multiproxy!");
        achievements = Storager.update(achievements, id, true);
        MultiUtils.getProxyConnector().addToQueue("UPDATE achievements SET achievements='%s' WHERE player_name='%s'",
                achievements, owner);
    }
    
    public void checkAndInvalidate() {
        Infractions infs = Infractions.infractions.get(owner.toLowerCase());
        if(infs == null)
            invalidate(owner);
    }
    
    public static class GotAchievementRunnable {
        
        public void got(String player, Achievement a) {
            //Nothing
        }
        
    }
    
}
