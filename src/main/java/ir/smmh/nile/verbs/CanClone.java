package ir.smmh.nile.verbs;

import ir.smmh.util.FunctionalUtil;

public interface CanClone<T> extends FunctionalUtil.RecursivelySpecific<T> {
    T clone(boolean deepIfPossible);

    static <T> T clone(CanClone<T> cloneable, boolean deepIfPossible) {
        return cloneable.clone(deepIfPossible);
    }
}
