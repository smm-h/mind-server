package ir.smmh.nile.verbs;

import ir.smmh.util.Mutable;

public interface CanRemoveAtPlace<P> extends CanContainPlace<P>, Mutable {

    static <P> void removeAtPlace(CanRemoveAtPlace<P> canRemoveAtPlace, P toRemove) {
        canRemoveAtPlace.removeAtPlace(toRemove);
    }

    void removeAtPlace(P toRemove);
}
