package ru.luvas.multiutils.player;

import java.lang.reflect.Constructor;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class ReflexiveSection<T extends Section> extends CachedSection<T> {
    
    private Constructor<T> constructor;
    
    public ReflexiveSection(Class<T> section) {
        try {
            this.constructor = section.getDeclaredConstructor(String.class);
            this.constructor.setAccessible(true);
        }catch(NoSuchMethodException | SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected T createNew(String owner) {
        try {
            return this.constructor.newInstance(owner);
        }catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
