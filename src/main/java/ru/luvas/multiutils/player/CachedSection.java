package ru.luvas.multiutils.player;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public abstract class CachedSection<T extends Section> {

    private final Map<String, T> cache = new HashMap<>();
    
    public final T getWithoutPreloading(String owner) {
        return cache.get(owner.toLowerCase());
    }
    
    public final boolean contains(String owner) {
        return cache.containsKey(owner.toLowerCase());
    }
    
    public final T get(String owner) {
        T t = cache.get(owner.toLowerCase());
        if(t != null)
            return t;
        t = createNew(owner);
        cache.put(owner.toLowerCase(), t);
        return t;
    }
    
    public final void invalidate(String owner) {
        cache.remove(owner.toLowerCase());
    }
    
    protected abstract T createNew(String owner);
    
}
