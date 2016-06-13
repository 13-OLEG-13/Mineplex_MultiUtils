package ru.luvas.multiutils.perms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.luvas.multiutils.MultiUtils;

/**
 *
 * @author RinesThaix
 */
public class PermissionList {
    
    private final static Map<String, PermissionList> lists = new HashMap<>();
    
    public static PermissionList get(String owner) {
        PermissionList list = lists.get(owner.toLowerCase());
        if(list != null)
            return list;
        list = new PermissionList(owner);
        lists.put(owner.toLowerCase(), list);
        return list;
    }
    
    public static PermissionList checkAndGet(String owner) {
        PermissionList list = lists.get(owner.toLowerCase());
        if(list != null)
            return list;
        try(ResultSet set = MultiUtils.getPermissionsConnector().query("SELECT groups FROM permissions WHERE player_name='" + owner + "'")) {
            if(set.next()) {
                list = new PermissionList(owner);
                lists.put(owner.toLowerCase(), list);
                return list;
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static void invalidate(String owner) {
        lists.remove(owner.toLowerCase());
    }

    private final String owner;
    private final List<PermissionGroup> permissions;
    
    private PermissionList(String owner) {
        this.owner = owner;
        this.permissions = new ArrayList<>();
        try(ResultSet set = MultiUtils.getPermissionsConnector().query("SELECT groups FROM permissions WHERE player_name='" + owner + "'")) {
            if(set.next()) {
                String[] spl = set.getString(1).split(" ");
                if(spl[0].equals("")) {
                    permissions.add(PermissionGroup.PLAYER);
                    updateGroupsInDatabase();
                }else {
                    for(String s : set.getString(1).split(" "))
                        this.permissions.add(PermissionGroup.getById(Integer.parseInt(s)));
                    sortPerms();
                }
            }else {
                permissions.add(PermissionGroup.PLAYER);
                updateGroupsInDatabase();
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public PermissionGroup getMainPermissionGroup() {
        return permissions.get(0);
    }
    
    public String getUncoloredShortPrefix() {
        PermissionGroup main = getMainPermissionGroup();
        if(hasGroup(PermissionGroup.BETA) && main.compareTo(PermissionGroup.YOUTUBE) < 0) {
            String prefix = main.getUncoloredShortPrefix();
            if(main == PermissionGroup.VIP_PLUS)
                prefix = "&3&lβeta &3";
            else if(main == PermissionGroup.RICH)
                prefix = "&b&lβeta &2";
            return prefix;
        }
        return main.getUncoloredShortPrefix();
    }
    
    public String getUncoloredLongPrefix() {
        PermissionGroup main = getMainPermissionGroup();
        if(hasGroup(PermissionGroup.BETA) && main.compareTo(PermissionGroup.YOUTUBE) < 0) {
            String prefix = main.getUncoloredLongPrefix();
            if(main == PermissionGroup.VIP_PLUS)
                prefix = "&3&lβeta &3";
            else if(main == PermissionGroup.RICH)
                prefix = "&b&lβeta &b";
            return prefix;
        }
        return main.getUncoloredLongPrefix();
    }
    
    public boolean hasPermissionOf(PermissionGroup pg) {
        return pg.getId() <= getMainPermissionGroup().getId();
    }
    
    public boolean hasGroup(PermissionGroup pg) {
        return permissions.contains(pg);
    }
    
    private void updateGroupsInDatabase() {
        StringBuilder sb = new StringBuilder();
        for(PermissionGroup perm : this.permissions)
            sb.append(perm.getId()).append(" ");
        String groups = sb.toString().trim();
        MultiUtils.getPermissionsConnector().addToQueue("INSERT INTO permissions VALUES ('%s', '%s') "
                + "ON DUPLICATE KEY UPDATE groups='%s'",
                owner, groups, groups);
    }
    
    public void removeGroup(PermissionGroup pg) {
        if(!this.permissions.contains(pg) || pg == PermissionGroup.PLAYER)
            return;
        this.permissions.remove(pg);
        updateGroupsInDatabase();
    }
    
    private final Comparator<PermissionGroup> comparator = new Comparator<PermissionGroup>() {

        @Override
        public int compare(PermissionGroup pg1, PermissionGroup pg2) {
            return pg1.getId() > pg2.getId() ? -1 : 1;
        }
        
    };
    
    public void addGroup(PermissionGroup pg) {
        if(this.permissions.contains(pg))
            return;
        this.permissions.add(pg);
        sortPerms();
        updateGroupsInDatabase();
    }
    
    public void invalidate() {
        lists.remove(owner.toLowerCase());
    }
    
    private void sortPerms() {
        PermissionGroup[] not_sorted = permissions.toArray(new PermissionGroup[permissions.size()]);
        Arrays.sort(not_sorted, comparator);
        permissions.clear();
        permissions.addAll(Arrays.asList(not_sorted));
    }
    
    public List<PermissionGroup> getAllGroups() {
        return permissions;
    }
    
}
