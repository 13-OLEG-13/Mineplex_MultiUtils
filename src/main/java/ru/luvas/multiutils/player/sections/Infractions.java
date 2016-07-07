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
public class Infractions extends Section {
    
    @Getter
    private int bans;
    
    @Getter
    private int mutes;
    
    @Getter
    private long banEnd;
    
    @Getter
    private long muteEnd;
    
    @Getter
    private String banCause;
    
    @Getter
    private String muteCause;
    
    @Getter
    private String banEnforcer;
    
    @Getter
    private String muteEnforcer;
    
    public Infractions(String owner) {
        super(owner);
        try(ResultSet set = MultiUtils.getProxyConnector().query("SELECT * FROM infractions WHERE player_name='" + owner + "'")) {
            if(set.next()) {
                bans = set.getInt("bans");
                mutes = set.getInt("mutes");
                banEnd = set.getLong("ban_end");
                muteEnd = set.getLong("mute_end");
                banCause = set.getString("ban_cause");
                muteCause = set.getString("mute_cause");
                banEnforcer = set.getString("ban_enforcer");
                muteEnforcer = set.getString("mute_enforcer");
            }else {
                bans = 0;
                mutes = 0;
                banEnd = 0l;
                muteEnd = 0l;
                banCause = "unknown";
                muteCause = "unknown";
                banEnforcer = "unknown";
                banCause = "unknown";
                updateInfractions();
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void updateInfractions() {
        MultiUtils.getProxyConnector().addToQueue("INSERT INTO infractions VALUES ('%s', '%s', '%s', %d, %d, '%s', '%s', %d, %d) "
                + "ON DUPLICATE KEY UPDATE "
                + "ban_cause='%s', ban_enforcer='%s', ban_end=%d, bans=%d, "
                + "mute_cause='%s', mute_enforcer='%s', mute_end=%d, mutes=%d",
                owner, banCause, banEnforcer, banEnd, bans, muteCause, muteEnforcer, muteEnd, mutes,
                banCause, banEnforcer, banEnd, bans, muteCause, muteEnforcer, muteEnd, mutes);
    }
    
    private void updateBanInfo() {
        MultiUtils.getProxyConnector().addToQueue("UPDATE infractions SET "
                + "ban_cause='%s', ban_enforcer='%s', ban_end=%d, bans=%d WHERE player_name='%s'",
                banCause, banEnforcer, banEnd, bans, owner);
    }
    
    private void updateMuteInfo() {
        MultiUtils.getProxyConnector().addToQueue("UPDATE infractions SET "
                + "mute_cause='%s', mute_enforcer='%s', mute_end=%d, mutes=%d WHERE player_name='%s'",
                muteCause, muteEnforcer, muteEnd, mutes, owner);
    }
    
    public void unban(boolean forcefully) {
        if(banEnd == 0)
            return;
        if(System.currentTimeMillis() < banEnd)
            bans = Math.max(0, bans - 1);
        banEnd = 0;
        banCause = "unknown";
        banEnforcer = "unknown";
        if(forcefully)
            updateBanInfo();
    }
    
    public void unmute(boolean forcefully) {
        if(muteEnd == 0)
            return;
        if(System.currentTimeMillis() < muteEnd)
            mutes = Math.max(0, mutes - 1);
        muteEnd = 0;
        muteCause = "unknown";
        muteEnforcer = "unknown";
        if(forcefully)
            updateMuteInfo();
    }
    
    public void ban(boolean forcefully, long end, String cause, String enforcer) {
        ++bans;
        banEnd = end;
        banCause = cause;
        banEnforcer = enforcer;
        if(forcefully)
            updateBanInfo();
    }
    
    public void mute(boolean forcefully, long end, String cause, String enforcer) {
        ++mutes;
        muteEnd = end;
        muteCause = cause;
        muteEnforcer = enforcer;
        if(forcefully)
            updateMuteInfo();
    }
    
    public boolean isBanned() {
        return banEnd > 0;
    }
    
    public boolean isMuted() {
        return muteEnd > 0;
    }
    
}
