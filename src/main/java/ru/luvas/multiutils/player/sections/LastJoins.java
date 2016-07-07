package ru.luvas.multiutils.player.sections;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Getter;
import ru.luvas.multiutils.MultiUtils;
import ru.luvas.multiutils.player.Section;

/**
 *
 * @author RinesThaix
 */
public class LastJoins extends Section {
    
    @Getter
    private long lastExit;
    
    @Getter
    private String lastIp;
    
    @Getter
    private String lastServer;
    
    public LastJoins(String owner) {
        super(owner);
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
    
}
