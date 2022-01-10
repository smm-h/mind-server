package ir.smmh.nile.verbs;

public interface CanAddTo<T> {

    static <T> void addTo(CanAddTo<T> canAddTo, T toAdd) {
        canAddTo.add(toAdd);
    }

    void add(T toAdd);
}
