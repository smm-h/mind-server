package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.Mutable;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class LazyCloneTest {

    private Sequential.Mutable<Integer> lazyClone;

    @SuppressWarnings("unchecked")
    private static <T extends CanClone<?> & Mutable.WithListeners> void cloneLazily(T canCloneLazily, boolean deepIfPossible, Consumer<T> setter) {
        setter.accept(canCloneLazily);
        canCloneLazily.getOnPreMutateListeners().addDisposable(() -> setter.accept((T) canCloneLazily.clone(deepIfPossible)));
    }

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
        cloneLazily(original, false, t -> lazyClone = t);

        // this clone is both equal to and the same object as the original
        assertEquals(original, lazyClone);
        assertSame(original, lazyClone);

        // but once you mutate the original object, it no longer is the same object
        original.setAtIndex(2, 5);
        assertNotSame(original, lazyClone);

        assertEquals("[1, 2, 5]", original.toString());
        assertEquals("[1, 2, 3]", eagerClone.toString());
        assertEquals("[1, 2, 3]", lazyClone.toString());

        /* known issue: if the lazyClone, which references the original object,
        is mutated before the original, the mutation occurs in the original
        reference only and not the lazyClone reference. This is not fixable */
    }
}