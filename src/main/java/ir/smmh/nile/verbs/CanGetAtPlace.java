package ir.smmh.nile.verbs;

import java.util.function.Function;

public interface CanGetAtPlace<P, T> extends CanContain<P>, Function<P, T> {

    static <P, T> T getAtPlace(CanGetAtPlace<P, T> canGetAtPlace, P place) {
        return canGetAtPlace.getAtPlace(place);
    }

    @Override
    default T apply(P place) {
        return getAtPlace(place);
    }

    T getAtPlace(P place);
}
