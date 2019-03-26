package org.isel.jingle.util.iterators;

import java.util.*;

public class IteratorCache<T> implements Iterator<T> {

    private Iterator<T> src;
    private static Map<Iterable<Object>, ArrayList<Object>> caches = new HashMap<Iterable<Object>, ArrayList<Object>>();
    private ArrayList<Object> cache;
    private int cacheIndex = 0;

    public IteratorCache(Iterable<T> iter) {
        this.src = iter.iterator();
        ArrayList<Object> cache = caches.get(iter);
        if(cache == null){
            cache = new ArrayList<>();
            caches.put((Iterable<Object>) iter, cache);
        }
        this.cache = cache;
    }

    @Override
    public boolean hasNext() {
        if(cacheIndex < cache.size() || src.hasNext())
            return true;
        return false;
    }

    @Override
    public T next() {
        if(cacheIndex < cache.size()){
            src.next();
            return (T) cache.get(cacheIndex++);
        }
        if(!src.hasNext()) throw new NoSuchElementException();
        T aux = src.next();
        cache.add(aux);
        return aux;
    }
}
