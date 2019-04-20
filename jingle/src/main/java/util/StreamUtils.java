package util;

import util.spliterators.SpliteratorCache;
import util.spliterators.SpliteratorMerge;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    public static <T> Supplier<Stream<T>> cache(Supplier<Stream<T>> srcSup){
        ArrayList<T> cache = new ArrayList<>();
        Spliterator<T> spliterator = srcSup.get().spliterator();
        return () -> {
            SpliteratorCache<T> res = new SpliteratorCache<>(spliterator, cache);
            return StreamSupport.stream(res, false);
        };
    }

    public static <T, R, U> Supplier<Stream<U>> merge(Supplier<Stream<T>> src1,
                                            Supplier<Stream<R>> src2,
                                            BiPredicate<T, R> predicate,
                                            BiFunction<T, R, U> mapper){
        return () -> {
            SpliteratorMerge<T, R, U> res = new SpliteratorMerge<>(src1.get().spliterator(), src2.get().spliterator(), predicate, mapper);
            return StreamSupport.stream(res, false);
        };
    }
}
