package ir.smmh.nile.verbs;

import ir.smmh.util.Mutable;

public interface CanRemovePlace<P> extends CanContainPlace<P>, Mutable {

    static <P> void removeAtPlace(CanRemovePlace<P> canRemovePlace, P toRemove) {
        canRemovePlace.removeAtPlace(toRemove);
    }

    void removeAtPlace(P toRemove);
}
