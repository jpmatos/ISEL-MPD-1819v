package org.isel.jingle;

import org.junit.Test;
import util.StreamUtils;

import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;

public class SpliteratorsTest {

    @Test
    public void spliteratorMergeTest(){
        String[] seq1 = {"isel", "ola", "dup", "super", "jingle"};
        Integer[] seq2 = {4, 5, 6, 7};
        String[] expectedSeq = {"isel4", "ola0", "dup0", "super5", "jingle6"};

        Supplier<Stream<String>> supRes = StreamUtils.merge(
                () -> Stream.of(seq1),
                () -> Stream.of(seq2),
                (str, nr) -> str.length() == nr,
                (str, nr) -> nr != null ? str + nr : str + 0);

        Object[] res = supRes.get().toArray();

        assertArrayEquals(expectedSeq, res);
    }
}
