package util.spliterators;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class SpliteratorMerge<T, R, U> extends Spliterators.AbstractSpliterator {
    private final Spliterator<T> src1;
    private final Spliterator<R> src2;
    private final ArrayList<R> src2Cache = new ArrayList<>();
    private final BiPredicate<T, R> predicate;
    private final BiFunction<T, R, U> mapper;

    public SpliteratorMerge(Spliterator<T> src1, Spliterator<R> src2, BiPredicate<T, R> predicate, BiFunction<T, R, U> mapper) {
        super(src1.estimateSize(), src1.characteristics());
        this.src1 = src1;
        this.src2 = src2;
        this.predicate = predicate;
        this.mapper = mapper;
    }

    @Override
    public boolean tryAdvance(Consumer action) {
        return src1.tryAdvance(item -> {

            //iterate cache
            for (R aux : src2Cache) {
                if (predicate.test(item, aux)) {
                    action.accept(mapper.apply(item, aux));
                    return;
                }
            }

            //cache is out, iterate src2
            final R[] outItem = (R[])new Object[1];
            while(src2.tryAdvance(newItem -> {src2Cache.add(newItem); outItem[0] = newItem;})){
                if (predicate.test(item, outItem[0])) {
                    action.accept(mapper.apply(item, outItem[0]));
                    return;
                }
            }

            //no value passes predicate, accept with null
            action.accept(mapper.apply(item, null));
        });
    }
}
