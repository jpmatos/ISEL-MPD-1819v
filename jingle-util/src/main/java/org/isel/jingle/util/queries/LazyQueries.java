/*
 * GNU General Public License v3.0
 *
 * Copyright (c) 2019, Miguel Gamboa (gamboa.pt)
 *
 *   All rights granted under this License are granted for the term of
 * copyright on the Program, and are irrevocable provided the stated
 * conditions are met.  This License explicitly affirms your unlimited
 * permission to run the unmodified Program.  The output from running a
 * covered work is covered by this License only if the output, given its
 * content, constitutes a covered work.  This License acknowledges your
 * rights of fair use or other equivalent, as provided by copyright law.
 *
 *   You may make, run and propagate covered works that you do not
 * convey, without conditions so long as your license otherwise remains
 * in force.  You may convey covered works to others for the sole purpose
 * of having them make modifications exclusively for you, or provide you
 * with facilities for running those works, provided that you comply with
 * the terms of this License in conveying all material for which you do
 * not control copyright.  Those thus making or running the covered works
 * for you must do so exclusively on your behalf, under your direction
 * and control, on terms that prohibit them from making any copies of
 * your copyrighted material outside their relationship with you.
 *
 *   Conveying under any other circumstances is permitted solely under
 * the conditions stated below.  Sublicensing is not allowed; section 10
 * makes it unnecessary.
 *
 */

package org.isel.jingle.util.queries;

import org.isel.jingle.util.iterators.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LazyQueries {

    public static <T> Iterable<T> filter(Iterable<T> src, Predicate<T> pred){
        return () -> new IteratorFilter<>(src, pred);
    }

    public static <T> Iterable<T> skip(Iterable<T> src, int nr){
        return () -> {
            Iterator<T> iter = src.iterator();
            int count = nr;
            while(count-- > 0 && iter.hasNext()) iter.next();
            return iter;
        };
    }

    public static <T> Iterable<T> limit(Iterable<T> src, int nr){
        return () -> new IteratorLimit<>(src,nr);
    }

    public static <T, R> Iterable<R> map(Iterable<T> src, Function<T, R> mapper){
        return () -> new IteratorMap<>(src, mapper);
    }

    public static <T> Iterable<T> generate(Supplier<T> next){
        return () -> new IteratorGenerate<>(next);
    }

    public static <T> Iterable<T> iterate(T seed, Function<T, T> next){
        return () -> new IteratorIterate<>(seed, next);
    }

    public static <T> int count(Iterable<T> src) {
        int count = 0;
        for (T item : src) count++;
        return count;
    }

    public static <T> Object[] toArray(Iterable<T> src) {
        LinkedList res = new LinkedList();
        for(T item : src) res.add(item);
        return res.toArray();
    }

    public static <T> Optional<T> first(Iterable<T> src) {
        Iterator<T> iter = src.iterator();
        return iter.hasNext() ? Optional.of(iter.next()) : Optional.empty();
    }

    public static <T extends Comparable<T>> Optional<T> max(Iterable<T> src) {
        T max = null;
        for (T current : src) {
            if (max == null) {
                max = current;
                continue;
            }
            if (current.compareTo(max) > 0)
                max = current;
        }
        return Optional.of(max);
    }

    public static <T> Iterable<T> from(T[] items) {
        return () -> new IteratorFrom<>(items);
    }

    public static <T> Iterable<T> takeWhile(Iterable<T> src, Predicate<T> pred){
        return () -> new IteratorTakeWhile<>(src, pred);
    }
    public static <T, R> Iterable<R> flatMap(Iterable<T> src, Function<T, Iterable<R>> mapper){
        return () -> new IteratorFlatMap<>(src, mapper);
    }

    public static <T> T last(Iterable<T> src) {
        T res = null;
        for (T item : src){
            res = item;
        }
        return res;
    }

    public static <T> Iterable<T> cache(Iterable<T> iter){
        ArrayList<T> cache = new ArrayList<>();
        Iterator<T> src = iter.iterator();
        return () -> new IteratorCache<T>(src, cache);
    }
}
