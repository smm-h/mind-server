package ir.smmh.nile.verbs;

import ir.smmh.util.Mutable;

public interface CanRemovePlace<P, T> extends CanContainPlace<P>, Mutable {

    static <P, T> void removeAtPlace(CanRemovePlace<P, T> canRemovePlace, P toRemove) {
        canRemovePlace.removeAtPlace(toRemove);
    }

    void removeAtPlace(P toRemove);
}
