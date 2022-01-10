package ir.smmh.util.jile;

/**
 * The base class for "this or that" object, which holds exactly one object of
 * type either This or That.
 */
public interface Or<This, That> {

    static <T> T generalize(Or<? extends T, ? extends T> or) {
        return or.isThis() ? or.getThis() : or.getThat();
    }

    boolean isThis();

    This getThis();

    That getThat();

    default boolean sameAs(Or<This, That> other) {
        return isThis() == other.isThis();
    }
}
