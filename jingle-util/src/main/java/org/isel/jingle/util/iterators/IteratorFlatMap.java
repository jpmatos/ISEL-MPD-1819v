package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class IteratorFlatMap<T, R> implements Iterator<R> {
    private Iterator<T> items;
    private Iterator<R> mapped = null;
    private Function<T, Iterable<R>> mapper;

    public IteratorFlatMap(Iterable<T> src, Function<T, Iterable<R>> mapper) {
        this.items = src.iterator();
        this.mapper = mapper;
    }
    @Override
    public boolean hasNext() {
        if(mapped == null) {
            if (!items.hasNext())
                return false;
            mapped = mapper.apply(items.next()).iterator();
        }

        if(!mapped.hasNext()){
            while(!mapped.hasNext() && items.hasNext())
                mapped = mapper.apply(items.next()).iterator();
            return mapped.hasNext();
        }
        return true;
    }

    @Override
    public R next() {
        if(!hasNext()) throw new NoSuchElementException();
        return mapped.next();
    }
}
