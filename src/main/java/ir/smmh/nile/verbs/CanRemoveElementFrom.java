package ir.smmh.nile.verbs;

public interface CanRemoveElementFrom<T> {

    static <T> void removeFrom(CanRemoveElementFrom<T> canRemoveElementFrom, T toRemove) {
        canRemoveElementFrom.removeElementFrom(toRemove);
    }

    void removeElementFrom(T toRemove);
}
