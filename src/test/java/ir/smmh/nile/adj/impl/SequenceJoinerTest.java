package ir.smmh.nile.adj.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.FunctionalUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequenceJoinerTest {

    SequenceJoiner<Character> joiner = new SequenceJoiner<>();

    @Test
    void test() {
        joiner.join(Sequential.of("Hello"));
        joiner.join(Sequential.of(", "));
        joiner.join(Sequential.of("World"));
        joiner.join(Sequential.of("!"));
        assertEquals(13, joiner.getSize());
        assertEquals("Hello, World!", new String(joiner.toCharArray(FunctionalUtil::itself)));
    }

}