package ru.luvas.multiutils.serverlist;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author RinesThaix
 */
@AllArgsConstructor
public class ServerInfo {

    @Getter
    private final String name;
    
    @Getter
    private final String ip;
    
    @Getter
    private final int port;
    
}
