package ir.smmh.nile.verbs;

public interface CanSwapAtPlaces<P, T> extends CanSetAtPlace<P, T>, CanGetAtPlace<P, T> {

    static <P, T> void swap(CanSwapAtPlaces<P, T> canSwapAtPlaces, P i, P j) {
        canSwapAtPlaces.swap(i, j);
    }

    default void swap(P i, P j) {
        T temp = getAtPlace(i);
        setAtPlace(i, getAtPlace(j));
        setAtPlace(j, temp);
    }
}
