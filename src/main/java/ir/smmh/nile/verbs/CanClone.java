package ir.smmh.nile.verbs;

import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.Mutable;

import java.util.function.Consumer;

public interface CanClone<T> extends FunctionalUtil.RecursivelySpecific<T> {
    static <T> T clone(CanClone<T> cloneable, boolean deepIfPossible) {
        return cloneable.clone(deepIfPossible);
    }

    T clone(boolean deepIfPossible);
}
