package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.function.Supplier;

public class IteratorGenerate<T> implements Iterator<T> {
    private T res;
    private Supplier<T> next;

    public IteratorGenerate(Supplier<T> next) {
        this.next = next;
    }

    @Override
    public boolean hasNext() {
        if(res == null)
            res = next.get();
        return res != null;
    }

    @Override
    public T next() {
        if(res == null)
            return next.get();
        T cur = res;
        res = null;
        return cur;
    }
}
