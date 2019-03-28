package org.isel.jingle.util.iterators;

import java.util.*;

public class IteratorCache<T> implements Iterator<T> {

    private Iterator<T> src;
    private ArrayList<T> cache;
    private int cacheIndex = 0;

    public IteratorCache(Iterator<T> src, ArrayList<T> cache) {
        this.src = src;
        //this.cacheIndex = 0;
        this.cache = cache;
    }

    @Override
    public boolean hasNext() {
        if(cacheIndex < cache.size())
            return true;
        return src.hasNext();
    }

    @Override
    public T next() {
        if(cacheIndex < cache.size()){
            //if(src.hasNext()) src.next();
            return cache.get(cacheIndex++);
        }
        if(!src.hasNext()) throw new NoSuchElementException();
        T aux = src.next();
        cache.add(aux);
        cacheIndex++;
        return aux;
    }
}
