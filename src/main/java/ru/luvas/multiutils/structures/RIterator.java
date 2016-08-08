package ru.luvas.multiutils.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class represents cyclic iterator
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class RIterator<T> implements Iterator<T> {
    
    private final Collection<T> collection;
    private Iterator<T> iterator;
    private final boolean createdByCollection;
    
    public RIterator(Collection<T> collection) {
        this.collection = collection;
        this.iterator = collection.iterator();
        this.createdByCollection = true;
    }
    
    public RIterator(Iterator<T> iterator) {
        collection = new ArrayList<>();
        while(iterator.hasNext())
            collection.add(iterator.next());
        this.iterator = collection.iterator();
        this.createdByCollection = false;
    }
    
    public RIterator(T... instances) {
        collection = new ArrayList<>();
        collection.addAll(Arrays.asList(instances));
        this.iterator = collection.iterator();
        this.createdByCollection = false;
    }

    /**
     * @readdAArrays.asList(instances)ln true whether collection is not empty
     */
    @Override
    public boolean hasNext() {
        return !collection.isEmpty();
    }
    
    @Override
    public T next() {
        if(!iterator.hasNext())
            iterator = collection.iterator();
        return iterator.next();
    }
    
    /**
     * Use only on RIterator created by Collection, not by Iterator.
     */
    @Override
    public void remove() {
        if(createdByCollection)
            iterator.remove();
        else
            throw new IllegalStateException("You can not use remove() method on RIterator, created by Iterator");
    }
    
    public int size() {
        return collection.size();
    }
    
}
