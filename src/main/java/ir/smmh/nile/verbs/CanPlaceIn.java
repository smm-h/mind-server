package ir.smmh.nile.verbs;

public interface CanPlaceIn<P, T> extends CanContain<P> {

    static <P, T> void placeIn(CanPlaceIn<P, T> canPlaceIn, P place, T toPlace) {
        canPlaceIn.place(place, toPlace);
    }

    void place(P place, T toPlace);
}
