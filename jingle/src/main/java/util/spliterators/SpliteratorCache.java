package util.spliterators;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class SpliteratorCache<T> extends Spliterators.AbstractSpliterator {
    private final Spliterator<T> iter;
    private ArrayList<T> cache;
    private int cacheIndex = 0;

    public SpliteratorCache(Spliterator<T> src, ArrayList<T> cache) {
        super(src.estimateSize(), src.characteristics());
        this.cache = cache;
        iter = src;
    }

    @Override
    public boolean tryAdvance(Consumer action) {
        if(cacheIndex < cache.size()){
            action.accept(cache.get(cacheIndex++));
            return true;
        }
        return iter.tryAdvance(item -> {
            cacheIndex++;
            cache.add(item);
            action.accept(item);
        });
    }
}
