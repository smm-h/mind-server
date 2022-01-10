package ir.smmh.nile.verbs;

public interface CanRemoveIndexFrom<T> {

    static <T> void removeIndexFrom(CanRemoveIndexFrom<T> canRemoveIndexFrom, int toRemove) {
        canRemoveIndexFrom.removeIndexFrom(toRemove);
    }

    void removeIndexFrom(int toRemove);
}
