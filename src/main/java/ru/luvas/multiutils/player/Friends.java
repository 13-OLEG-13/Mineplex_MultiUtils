package ru.luvas.multiutils.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.luvas.multiutils.achievements.Achievement;
import ru.luvas.multiutils.achievements.Achievements;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.RSocketConnector;
import ru.luvas.multiutils.sockets.packets.Packet3Friends;
import ru.luvas.multiutils.sockets.packets.Packet3Friends.Action;

/**
 *
 * @author RinesThaix
 */
public class Friends {

    private final static Map<String, Friends> friends = new HashMap<>();
    
    public static Friends getClearly(String owner) {
        return friends.get(owner.toLowerCase());
    }
    
    public static Friends get(String owner) {
        Friends f = friends.get(owner.toLowerCase());
        if(f != null)
            return f;
        f = new Friends(owner);
        friends.put(owner.toLowerCase(), f);
        return f;
    }
    
    public static void invalidate(String owner) {
        friends.remove(owner.toLowerCase());
    }
    
    @Getter
    private final String owner;
    
    @Getter
    private List<FriendInfo> info = new ArrayList<>();
    
    @Getter
    private List<String> requests = new ArrayList<>();
    
    public Friends(String owner) {
        this.owner = owner;
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
        for(String s : requests)
            if(s.equalsIgnoreCase(player))
                return true;
        return false;
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
        for(FriendInfo friend : info)
            if(friend.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }
    
    public void updateNewFriend(String player, long lastOnline, String currentServer) {
        boolean online = lastOnline == 0;
        info.add(new FriendInfo(player, online, lastOnline, currentServer));
        Achievements achs = Achievements.get(owner);
        achs.addAchievement(Achievement.FIRST_FRIEND);
        achs.checkAndInvalidate();
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
        for(FriendInfo friend : info)
            if(friend.getName().equalsIgnoreCase(player)) {
                friend.online = lastOnline == 0;
                friend.lastOnline = lastOnline;
                friend.currentServer = currentServer;
            }
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
