package ru.luvas.multiutils;

import lombok.Getter;
import lombok.Setter;
import ru.luvas.multiutils.sql.Connector;
import ru.luvas.multiutils.sql.ConnectorBuilder;

/**
 *
 * @author RinesThaix
 */
public class MultiUtils {
    
    @Getter
    private static Connector permissionsConnector;
    
    @Getter
    private static Connector gamesConnector;
    
    @Getter
    private static Connector proxyConnector;
    
    @Setter
    @Getter
    private static boolean inDebugMode = false;

    public static void initPermissionsConnector(String host, int port, String user, String pass, String db) {
        permissionsConnector = new ConnectorBuilder()
                .setName("PermissionsSQL")
                .setHost(host + ":" + port)
                .setUser(user)
                .setPassword(pass)
                .setDatabase(db)
                .setAutoReconnect(true)
                .setAutoReconnectRetries(10)
                .build(true);
    }

    public static void initGamesConnector(String host, int port, String user, String pass, String db) {
        gamesConnector = new ConnectorBuilder()
                .setName("GamesSQL")
                .setHost(host + ":" + port)
                .setUser(user)
                .setPassword(pass)
                .setDatabase(db)
                .setAutoReconnect(true)
                .setAutoReconnectRetries(10)
                .build(true);
    }

    public static void initProxyConnector(String host, int port, String user, String pass, String db) {
        proxyConnector = new ConnectorBuilder()
                .setName("ProxySQL")
                .setHost(host + ":" + port)
                .setUser(user)
                .setPassword(pass)
                .setDatabase(db)
                .setAutoReconnect(true)
                .setAutoReconnectRetries(10)
                .build(true);
    }
    
}
