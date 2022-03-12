package ir.smmh.util.jile.impl;

import ir.smmh.util.jile.Or;

/**
 * Takes half the size in memory, but does one unchecked cast per get.
 *
 * @see FatOr
 */
public class SlimOr<This, That> extends AbstractOr<This, That> {

    private final Object object;
    private final boolean isThis;

    public SlimOr(Object object, boolean isThis) {
        super();
        this.object = object;
        this.isThis = isThis;
    }

    public static <This, That> Or<This, That> makeThis(This object) {
        return either(object, null);
    }

    public static <This, That> Or<This, That> makeThat(That object) {
        return either(null, object);
    }

    public static <This, That> Or<This, That> either(This thisObject, That thatObject) {
        if ((thisObject == null) == (thatObject == null))
            throw new IllegalArgumentException("either both or neither of values are null");
        boolean isThis = thatObject == null;
        return new SlimOr<>(isThis ? thisObject : thatObject, isThis);
    }

    @Override
    public final boolean isThis() {
        return isThis;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final This getThis() {
        return (This) object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final That getThat() {
        return (That) object;
    }
}
