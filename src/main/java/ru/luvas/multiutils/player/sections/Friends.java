package ru.luvas.multiutils.player.sections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.luvas.multiutils.achievements.Achievement;
import ru.luvas.multiutils.achievements.Achievements;
import ru.luvas.multiutils.player.PlayerDatas;
import ru.luvas.multiutils.player.Section;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.RSocketConnector;
import ru.luvas.multiutils.sockets.packets.Packet3Friends;
import ru.luvas.multiutils.sockets.packets.Packet3Friends.Action;

/**
 *
 * @author RinesThaix
 */
public class Friends extends Section {
    
    @Getter
    private List<FriendInfo> info = new ArrayList<>();
    
    @Getter
    private List<String> requests = new ArrayList<>();
    
    public Friends(String owner) {
        super(owner);
        if(RSocketConnector.getConnectorMode() == RSocketConnector.ConnectorMode.CLIENT) {
            RIndependentClient.getInstance().send(new Packet3Friends(owner, Action.GET, "null", "null") {
            
                @Override
                public void execute() {
                    info = getFriends();
                    requests = getFriendRequests();
                }
            
            }, owner);
        }else
            throw new IllegalStateException("This class mustn't be used by the multiproxy!");
    }
    
    public boolean hasRequest(String player) {
        return requests.stream().anyMatch(s -> s.equalsIgnoreCase(player));
    }
    
    public void addRequest(String player) {
        if(hasRequest(player))
            return;
        RIndependentClient.getInstance().send(new Packet3Friends(owner, Action.ADD, player, owner), owner);
    }
    
    public void removeRequest(String player) {
        if(!hasRequest(player))
            return;
        RIndependentClient.getInstance().send(new Packet3Friends(owner, Action.REMOVE, player, "null"), owner);
    }
    
    public void addFriend(String player) {
        if(isFriend(player))
            return;
        RIndependentClient.getInstance().send(new Packet3Friends(owner, Action.ADD, player, owner), owner);
    }
    
    public void removeFriend(String player) {
        if(!isFriend(player))
            return;
        RIndependentClient.getInstance().send(new Packet3Friends(owner, Action.REMOVE, player, "null"), owner);
    }
    
    public boolean isFriend(String name) {
        return info.stream().map(FriendInfo::getName).anyMatch(fn -> fn.equalsIgnoreCase(name));
    }
    
    public void updateNewFriend(String player, long lastOnline, String currentServer) {
        boolean online = lastOnline == 0;
        info.add(new FriendInfo(player, online, lastOnline, currentServer));
        Achievements achs = PlayerDatas.get(Achievements.class, owner);
        achs.addAchievement(Achievement.FIRST_FRIEND);
        if(PlayerDatas.getWithoutPreloading(Infractions.class, owner) == null)
            PlayerDatas.invalidate(Achievements.class, owner);
    }
    
    public void updateOldFriend(String player) {
        for(Iterator<FriendInfo> iterator = info.iterator(); iterator.hasNext();) {
            if(iterator.next().getName().equalsIgnoreCase(player)) {
                iterator.remove();
                break;
            }
        }
    }
    
    public void update(String player, long lastOnline, String currentServer) {
        info.stream().filter(f -> f.getName().equalsIgnoreCase(player)).forEach(f -> {
            f.online = lastOnline == 0;
            f.lastOnline = lastOnline;
            f.currentServer = currentServer;
        });
    }
    
    public void updateNewRequest(String player) {
        requests.add(player);
    }
    
    public void updateOldRequest(String player) {
        requests.remove(player);
    }
    
    @AllArgsConstructor
    public static class FriendInfo {
        
        @Getter
        private final String name;
        
        @Getter
        private boolean online;
        
        @Getter
        private long lastOnline;
        
        @Getter
        private String currentServer;
        
    }
    
}
