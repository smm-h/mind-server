package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Multitude;

import java.util.function.Function;

public interface CanGetAtPlace<P, T> extends CanContainPlace<P>, Function<P, T>, Multitude {

    static <P, T> T getAtPlace(CanGetAtPlace<P, T> canGetAtPlace, P place) {
        return canGetAtPlace.getAtPlace(place);
    }

    @Override
    default T apply(P place) {
        return getAtPlace(place);
    }

    T getAtPlace(P place);
}
