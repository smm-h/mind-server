package ir.smmh.nile.verbs;

import ir.smmh.util.FunctionalUtil;

public interface CanClone<T> extends FunctionalUtil.RecursivelySpecific<T> {
    static <T> T clone(CanClone<T> cloneable, boolean deepIfPossible) {
        return cloneable.clone(deepIfPossible);
    }

    T clone(boolean deepIfPossible);
}
