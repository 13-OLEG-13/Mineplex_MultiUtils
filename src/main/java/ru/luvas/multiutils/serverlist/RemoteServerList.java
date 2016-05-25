package ru.luvas.multiutils.serverlist;

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
public abstract class RemoteServerList {

    @Getter
    private final Map<String, ServerInfo> servers = new HashMap<>();
    
    public RemoteServerList() {
        try(ResultSet set = MultiUtils.getProxyConnector().query("SELECT * FROM servers")) {
            while(set.next()) {
                ServerInfo si = new ServerInfo(set.getString("server_name"), set.getString("ip"), set.getInt("port"));
                servers.put(si.getName(), si);
                onAdd(si);
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    protected abstract void onAdd(ServerInfo si);
    
    protected abstract void onRemove(ServerInfo si);
    
    public void add(ServerInfo si, boolean forcefully) {
        String name = si.getName();
        if(servers.containsKey(name))
            throw new IllegalStateException("Server with name '" + name + "' already exists!");
        servers.put(name, si);
        onAdd(si);
        if(forcefully)
            MultiUtils.getProxyConnector().addToQueue("INSERT INTO servers VALUES ('%s', '%s', %d)",
                    si.getName(), si.getIp(), si.getPort());
    }
    
    public void remove(String name, boolean forcefully) {
        if(!servers.containsKey(name))
            throw new IllegalStateException("Server with name '" + name + "' doesn't exist!");
        ServerInfo si = servers.remove(name);
        onRemove(si);
        if(forcefully)
            MultiUtils.getProxyConnector().addToQueue("DELETE FROM servers WHERE server_name='%s'", si.getName());
    }
    
    public ServerInfo getServer(String name) {
        return servers.get(name);
    }
    
    public ServerInfo getServer(String ip, int port) {
        for(ServerInfo si : servers.values())
            if(si.getIp().equals(ip) && si.getPort() == port)
                return si;
        return null;
    }
    
}
