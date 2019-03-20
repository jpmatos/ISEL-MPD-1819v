package org.isel.jingle.util.iterators;

import java.util.Iterator;

public class IteratorFrom<T> implements Iterator<T> {
    private T[] src;
    private int index = 0;

    public IteratorFrom(T[] items) {
        this.src = items;
    }

    @Override
    public boolean hasNext() {
        return index < src.length;
    }

    @Override
    public T next() {
        T temp = src[index];
        index++;
        return temp;
    }
}
