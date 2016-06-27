package ru.luvas.multiutils.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.luvas.multiutils.sockets.RIndependentClient;
import ru.luvas.multiutils.sockets.packets.Packet5Parties;
import ru.luvas.multiutils.sockets.packets.Packet5Parties.Action;

/**
 *
 * @author RinesThaix
 */
public class Party {

    @Getter
    private final static Map<String, Party> parties = new HashMap<>();
    
    public static Party get(String owner) {
        return parties.get(owner.toLowerCase());
    }
    
    public static void create(String owner) {
        RIndependentClient.getInstance().send(new Packet5Parties(owner, Action.CREATE, "null", null), owner);
    }
    
    public static void acceptInvitation(String player) {
        RIndependentClient.getInstance().send(new Packet5Parties(player, Action.ACCEPT, "null", null), player);
    }
    
    public static void denyInvitation(String player) {
        RIndependentClient.getInstance().send(new Packet5Parties(player, Action.DENY, "null", null), player);
    }
    
    public static void updateCreation(String owner, String coloredName, String server) {
        parties.put(owner.toLowerCase(), new Party(owner, coloredName, server));
    }
    
    public static void updateRecreation(List<PartyMember> members) {
        Party p = new Party(members);
        members.stream().map(m -> m.getName().toLowerCase()).forEach(m -> parties.put(m, p));
    }
    
    @Getter
    private String owner;
    
    @Getter
    private final List<PartyMember> members;
    
    private Party(String owner, String coloredName, String server) {
        this.owner = owner;
        members = new ArrayList<>();
        members.add(new PartyMember(owner, coloredName, server));
    }
    
    private Party(List<PartyMember> members) {
        this.owner = members.get(0).getName();
        this.members = members;
    }
    
    public PartyMember getLeader() {
        return members.get(0);
    }
    
    public boolean isLeader(String name) {
        return getLeader().getName().equalsIgnoreCase(name);
    }
    
    public boolean isInParty(String name) {
        for(PartyMember member : members)
            if(member.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }
    
    public void invitePlayer(String player) {
        RIndependentClient.getInstance().send(new Packet5Parties(owner, Action.INVITE, player, null), owner);
    }
    
    public void leave(String player) {
        RIndependentClient.getInstance().send(new Packet5Parties(owner, Action.LEAVE, player, null), owner);
    }
    
    public void kick(String player) {
        RIndependentClient.getInstance().send(new Packet5Parties(owner, Action.KICK, player, null), owner);
    }
    
    public void disband() {
        RIndependentClient.getInstance().send(new Packet5Parties(owner, Action.DISBAND, "null", null), owner);
    }
    
    public void changeLeader(String new_leader) {
        RIndependentClient.getInstance().send(new Packet5Parties(owner, Action.CHANGE_LEADER, new_leader, null), owner);
    }
    
    public void updateNewMember(String name, String coloredName, String server) {
        members.add(new PartyMember(name, coloredName, server));
        parties.put(name.toLowerCase(), this);
    }
    
    public void updateOldMember(String name) {
        for(Iterator<PartyMember> iterator = members.iterator(); iterator.hasNext();) {
            String iname = iterator.next().getName();
            if(iname.equalsIgnoreCase(name)) {
                iterator.remove();
                parties.remove(iname.toLowerCase());
                break;
            }
        }
    }
    
    public void updateLeader(String name) {
        int id = 0;
        PartyMember member = null;
        for(int i = 0; i < members.size(); ++i) {
            if(members.get(i).getName().equalsIgnoreCase(name)) {
                id = i;
                member = members.get(i);
                break;
            }
        }
        if(member == null)
            return;
        members.set(id, members.get(0));
        members.set(0, member);
        this.owner = name;
    }
    
    public void updateDisbanded() {
        members.stream().map(m -> m.getName().toLowerCase()).forEach(parties::remove);
        members.clear();
    }
    
    public void updateServer(String player, String server) {
        for(PartyMember member : members)
            if(member.getName().equalsIgnoreCase(player)) {
                member.server = server;
                return;
            }
    }
    
    @Data
    @AllArgsConstructor
    public static class PartyMember {
        
        private final String name;
        
        private final String coloredName;
        
        private String server;
        
    }
    
}
