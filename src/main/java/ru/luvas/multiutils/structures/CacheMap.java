package ru.luvas.multiutils.structures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.luvas.multiutils.sockets.RSocketConnector;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
/**
 * Map that automatically cleans objects that were not accessed for last N seconds.
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author RinesThaix
 */
public class CacheMap<K, V> extends HashMap<K, V> {

    private Map<K, Long> lastAccess = new HashMap<>();
    
    public CacheMap(final long existenceTimeInSeconds) {
        if(existenceTimeInSeconds < 5)
            throw new IllegalArgumentException("Existence time must not be less than 5s!");
        RSocketConnector.getExecutorService().execute(new Runnable() {

            @Override
            public void run() {
                final long current = System.currentTimeMillis();
                Set<K> toSafeRemove = new HashSet<>();
                synchronized(lastAccess) {
                    for(K key : keySet()) {
                        Long l = lastAccess.get(key);
                        if(l == null)
                            l = 0l;
                        if(current - l > existenceTimeInSeconds * 1000)
                            toSafeRemove.add(key);
                    }
                    for(K key : toSafeRemove)
                        remove(key);
                }
                try {
                    Thread.sleep(5000l);
                }catch(InterruptedException ex) {}
            }
            
        });
    }
    
    @Override
    public V put(K key, V value) {
        synchronized(lastAccess) {
            V v = super.put(key, value);
            lastAccess.put(key, System.currentTimeMillis());
            return v;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        synchronized(lastAccess) {
            V v = super.get(key);
            if(v != null)
                lastAccess.put((K) key, System.currentTimeMillis());
            return v;
        }
    }
    
    @Override
    public V remove(Object key) {
        synchronized(lastAccess) {
            V v = super.remove(key);
            if(v != null)
                lastAccess.remove(key);
            return v;
        }
    }
    
}