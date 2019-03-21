package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorIterate<T> implements Iterator<T> {
    T curr;
    Function<T, T> next;

    public IteratorIterate(T seed, Function<T, T> next) {
        this.curr = seed;
        this.next = next;
    }

    public boolean hasNext() {
        return true;
    }

    public T next() {
        T tmp = curr;
        curr = next.apply(tmp);
        return tmp;
    }
}
