package ir.smmh.nile.verbs;

public interface CanPrependTo<T> {

    static <T> void prependedTo(CanPrependTo<T> canPrependTo, T toPrepend) {
        canPrependTo.prepend(toPrepend);
    }

    void prepend(T toPrepend);
}
