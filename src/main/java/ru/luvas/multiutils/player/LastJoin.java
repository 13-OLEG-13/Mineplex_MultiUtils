package ru.luvas.multiutils.player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import ru.luvas.multiutils.MultiUtils;

/**
 *
 * @author RinesThaix
 */
public class LastJoin {

    private final static Map<String, LastJoin> joins = new HashMap<>();
    
    public static LastJoin get(String owner) {
        LastJoin join = joins.get(owner.toLowerCase());
        if(join != null)
            return join;
        join = new LastJoin(owner);
        joins.put(owner.toLowerCase(), join);
        return join;
    }
    
    public static void invalidate(String owner) {
        joins.remove(owner.toLowerCase());
    }
    
    @Getter
    private final String owner;
    
    @Getter
    private long lastExit;
    
    @Getter
    private String lastIp;
    
    @Getter
    private String lastServer;
    
    public LastJoin(String owner) {
        this.owner = owner;
        try(ResultSet set = MultiUtils.getProxyConnector().query("SELECT * FROM last_joins WHERE player_name='" + owner + "'")) {
            if(set.next()) {
                lastExit = set.getLong("last_exit");
                lastIp = set.getString("last_ip");
                lastServer = set.getString("last_server");
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateServer(String ip, String server) {
        MultiUtils.getProxyConnector().addToQueue("INSERT INTO last_joins VALUES ('%s', 0, '%s', '%s') "
                + "ON DUPLICATE KEY UPDATE last_ip='%s', last_server='%s', last_exit=0",
                owner, ip, server, ip, server);
        lastServer = server;
    }
    
    public void updateExit() {
        long current = System.currentTimeMillis();
        MultiUtils.getProxyConnector().addToQueue("UPDATE last_joins SET last_exit=%d WHERE player_name='%s'",
                current, owner);
        lastExit = current;
    }
    
    public void invalidate() {
        joins.remove(owner.toLowerCase());
    }
    
}
