package ru.luvas.multiutils.player;

import java.util.HashMap;
import java.util.Map;
import ru.luvas.multiutils.achievements.Achievements;
import ru.luvas.multiutils.player.sections.*;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@SuppressWarnings("unchecked")
public class PlayerDatas {
    
    private final static Map<Class<? extends Section>, CachedSection<? extends Section>> sections = new HashMap<>();
    
    static {
        register(Friends.class, new CachedSection<Friends>() {
            @Override
            protected Friends createNew(String owner) {
                return new Friends(owner);
            }
        });
        register(Infractions.class, new CachedSection<Infractions>() {
            @Override
            protected Infractions createNew(String owner) {
                return new Infractions(owner);
            }
        });
        register(LastJoins.class, new CachedSection<LastJoins>() {
            @Override
            protected LastJoins createNew(String owner) {
                return new LastJoins(owner);
            }
        });
        register(NetworkLeveling.class, new CachedSection<NetworkLeveling>() {
            @Override
            protected NetworkLeveling createNew(String owner) {
                return new NetworkLeveling(owner);
            }
        });
        register(Achievements.class, new CachedSection<Achievements>() {
            @Override
            protected Achievements createNew(String owner) {
                return new Achievements(owner);
            }
        });
    }
    
    public final static <T extends Section> void register(Class<T> clazz, CachedSection<T> section) {
        sections.put(clazz, section);
    }
    
    public final static <T extends Section> void register(Class<T> clazz) {
        sections.put(clazz, new ReflexiveSection<>(clazz));
    }
    
    public final static void unregister(Class<? extends Section> clazz) {
        sections.remove(clazz);
    }
    
    public final static <T extends Section> CachedSection<T> getCachedSection(Class<T> clazz) {
        return (CachedSection<T>) sections.get(clazz);
    }
    
    public final static <T extends Section> CachedSection<T> getCachedSection(T section) {
        return (CachedSection<T>) sections.get(section.getClass());
    }
    
    public final static <T extends Section> T get(Class<T> clazz, String owner) {
        return getCachedSection(clazz).get(owner);
    }
    
    public final static <T extends Section> T getWithoutPreloading(Class<T> clazz, String owner) {
        return getCachedSection(clazz).getWithoutPreloading(owner);
    }
    
    public final static <T extends Section> void invalidate(Class<T> clazz, String owner) {
        getCachedSection(clazz).invalidate(owner);
    }
    
    public final static void fullyInvalidate(String owner) {
        sections.values().forEach(cs -> cs.invalidate(owner));
    }

}
