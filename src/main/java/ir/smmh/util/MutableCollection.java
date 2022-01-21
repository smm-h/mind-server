package ir.smmh.util;

import ir.smmh.nile.verbs.CanAddTo;
import ir.smmh.nile.verbs.CanClear;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.nile.verbs.CanRemoveElementFrom;

public interface MutableCollection<T> extends
        Mutable.WithListeners,
        CanAddTo<T>,
        CanRemoveElementFrom<T>,
        CanContain<T>,
        CanClear,
        Iterable<T> {
}
