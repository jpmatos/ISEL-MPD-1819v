package util.spliterators;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class SpliteratorCache<T> extends Spliterators.AbstractSpliterator {
    private final Spliterator<T> iter;

    public SpliteratorCache(Spliterator<T> src, ArrayList<T> cache) {
        super(src.estimateSize(), src.characteristics());
        iter = src;
    }

    @Override
    public boolean tryAdvance(Consumer action) {
        //TODO Implement logic
        return false;
    }
}
