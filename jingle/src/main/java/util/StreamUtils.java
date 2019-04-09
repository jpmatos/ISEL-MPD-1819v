package util;

import util.spliterators.SpliteratorCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
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
}
