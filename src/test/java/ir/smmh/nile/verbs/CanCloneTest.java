package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Sequential;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanCloneTest {

    private Sequential.Mutable<Integer> lazyClone;

    @Test
    void lazyCloneTest() {

        // create a sequence of integers
        Sequential.Mutable<Integer> original = Sequential.Mutable.ofArguments(1, 2, 3);

        // create an eager clone of the original sequence
        var eagerClone = original.clone(false);

        // this clone is equal to the original but not the same object as it
        assertEquals(original, eagerClone);
        assertNotSame(original, eagerClone);

        // create a lazy clone of the original sequence
        lazyClone = CanClone.cloneLazily(original, false, t -> lazyClone = t);

        // this clone is both equal to and the same object as the original
        assertEquals(original, lazyClone);
        assertSame(original, lazyClone);

        // but once you mutate the original object, it no longer is the same object
        original.setAtIndex(2, 5);
        assertNotSame(original, lazyClone);

        assertEquals("[1, 2, 5]", original.toString());
        assertEquals("[1, 2, 3]", eagerClone.toString());
        assertEquals("[1, 2, 3]", lazyClone.toString());
    }
}