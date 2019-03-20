package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.function.Predicate;

public class IteratorTakeWhile<T> implements Iterator<T>{
    private Iterator<T> items;
    private Predicate<T> pred;
    private T next = null;
    private boolean open = true;

    public IteratorTakeWhile(Iterable<T> src, Predicate<T> pred) {
        this.items = src.iterator();
        this.pred = pred;
    }

    @Override
    public boolean hasNext() {
        if(!open) return false;
        if (next == null){
            if(items.hasNext()) {
                next = items.next();
                if (pred.test(next)) {
                    return true;
                }
            }
            return open = false;
        }
        return true;
    }

    @Override
    public T next() {
        T aux = next;
        next = null;
        return aux;
    }
}
