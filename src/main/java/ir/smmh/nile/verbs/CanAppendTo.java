package ir.smmh.nile.verbs;

public interface CanAppendTo<T> extends CanAddTo<T> {

    static <T> void appendedTo(CanAppendTo<T> canAppendTo, T toAppend) {
        canAppendTo.append(toAppend);
    }

    void append(T toAppend);

    @Override
    default void add(T toAdd) {
        append(toAdd);
    }
}
