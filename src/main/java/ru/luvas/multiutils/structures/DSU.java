package ru.luvas.multiutils.structures;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author RinesThaix
 */
public class DSU<T> {

    private int lastId = 0;
    private final Map<T, Node<T>> links = new HashMap<>();
    private int[] parents = new int[1], size = new int[1];
    
    public void newSet(T object) {
        Node<T> node = getNode(object);
        if(node.id >= parents.length)
            extend();
        parents[node.id] = node.id;
        size[node.id] = 1;
    }
    
    public int getSetId(T object) {
        return getSetId(getNode(object).id);
    }
    
    private int getSetId(int v) {
        if(v == parents[v])
            return v;
        return parents[v] = getSetId(parents[v]);
    }
    
    public void union(T A, T B) {
        int a = getSetId(A), b = getSetId(B);
        if(a != b) {
            if(size[a] < size[b]) {
                int c = a;
                a = b;
                b = c;
            }
            parents[b] = a;
            size[a] += size[b];
        }
    }
    
    private void extend() {
        int[] new_parents = new int[parents.length << 1];
        int[] new_size = new int[parents.length << 1];
        System.arraycopy(parents, 0, new_parents, 0, parents.length);
        System.arraycopy(size, 0, new_size, 0, parents.length);
        parents = new_parents;
        size = new_size;
    }
    
    private Node<T> getNode(T object) {
        Node<T> node = links.get(object);
        if(node != null)
            return node;
        node = new Node<>(object, lastId++);
        links.put(object, node);
        return node;
    }
    
    public void clear() {
        lastId = 0;
        links.clear();
        parents = new int[1];
        size = new int[1];
    }
    
    @Data
    private static class Node<T> {
        
        private final T object;
        private final int id;
        
    }
    
}
