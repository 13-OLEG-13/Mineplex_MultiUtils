package ru.luvas.multiutils.perms;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 *
 * @author RinesThaix
 */
public enum PermissionGroup {
    
    PLAYER(0, "&r&7", "&r&7", "&7Игрок", "Игроки", 1f),
    VIP(100, "&6&lVIP &6", "&6&lVIP &6", "&6VIP", "&6VIP", 2f),
    BETA(101, "&6&lβeta &6", "&6&lβeta &6", "&fБета-Тестер", "&fβeta", 2f),
    VIP_PLUS(200, "&3&lVIP+ &3", "&3&lVIP+ &3", "&3VIP+", "&3VIP+", 3f),
    RICH(300, "&b&lRICH &b", "&b&lRICH &b", "&bRICH", "&bRICH", 5f),
    YOUTUBE(350, "&6&lYOUTUBE &6", "&6&lYOUTUBE &6", "&6Youtube", "&6Youtubers", 5f),
    YOUTUBE_PLUS(351, "&6&lYOUTUBE+ &6", "&6&lYOUTUBE+ &6", "&6Youtube+", "&6Youtubers+", 6f),
    YOUTUBE_PLUS_PLUS(352, "&6&lYOUTUBE++ &6", "&6&lYOUTUBE++ &6", "&6Youtube++", "&6Youtubers++", 7f),
    SPECIAL(399, "&d&lUNIQUE &d", "&d&lUNIQUE &d", "&dВажная персона ", "&dUniques", 7f),
    BUILDER(400, "&e&lBUILDER &e", "&e&lBuilder &e", "&eСтроитель", "&eСтроители", 2f),
    SRBUILDER(450, "&6&lBUILDER+ &6", "&6&lBUILDER+ &6", "&6Ст. Строитель", "&6Ст. Строители", 3f),
    HELPER(500, "&9&lHELPER &9", "&9&lHELPER &9", "&9Хелпер", "&9Хелперы", 2f),
    MODERATOR(600, "&2&lMOD &2", "&2&lMOD &2", "&2Модератор", "&2Модераторы", 3f),
    DEVELOPER(700, "&c&lDEV &c", "&c&lDEV &c", "&cРазработчик", "&cРазработчики", 10f),
    ADMINISTRATOR(800, "&c&lADMIN &c", "&c&lADMIN &c", "&cАдминистратор", "&cАдминистраторы", 10f),
    OWNER(900, "&c&lOWNER ", "&c&lOWNER ", "&4Владелец", "&4Владельцы", 10f);
    
    private final static Map<Integer, PermissionGroup> byIds;
    
    static {
        byIds = new HashMap<>(values().length);
        for(PermissionGroup pg : values())
            byIds.put(pg.getId(), pg);
    }

    @Getter
    private final int id;
    
    @Getter
    private final String uncoloredShortPrefix;
    
    @Getter
    private final String uncoloredLongPrefix;
    
    @Getter
    private final String normalName;
    
    @Getter
    private final String pluralName;
    
    @Getter
    private final float coinsMultiplier;
    
    PermissionGroup(int id, String longPrefix, String shortPrefix, String normalName, String pluralName, float coinsMultiplier) {
        this.id = id;
        this.uncoloredShortPrefix = shortPrefix;
        this.uncoloredLongPrefix = longPrefix;
        this.normalName = normalName;
        this.pluralName = pluralName;
        this.coinsMultiplier = coinsMultiplier;
    }
    
    public final boolean isStaff() {
        return compareTo(PermissionGroup.BUILDER) >= 0;
    }
    
    public String getName() {
        return name();
    }
    
    public static PermissionGroup getById(int id) {
        return byIds.get(id);
    }
    
}
