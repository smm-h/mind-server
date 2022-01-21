package ir.smmh.nile.verbs;

import ir.smmh.util.Mutable;

public interface CanSetAtPlace<P, T> extends CanContainPlace<P>, Mutable {

    static <P, T> void setAtPlace(CanSetAtPlace<P, T> canSetAtPlace, P place, T toSet) {
        canSetAtPlace.setAtPlace(place, toSet);
    }

    void setAtPlace(P place, T toSet);
}
