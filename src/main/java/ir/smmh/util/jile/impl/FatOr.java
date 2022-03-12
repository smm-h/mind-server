package ir.smmh.util.jile.impl;

import ir.smmh.util.jile.Or;
import org.jetbrains.annotations.Nullable;

/**
 * Takes double the size in memory, but is checked
 *
 * @see SlimOr
 */
@SuppressWarnings("SuspiciousGetterSetter")
public class FatOr<This, That> extends AbstractOr<This, That> {

    private final @Nullable This thisObject;
    private final @Nullable That thatObject;
    private final boolean isThis;

    @SuppressWarnings("unchecked")
    protected FatOr(Object object, boolean isThis) {
        super();
        this.isThis = isThis;
        if (isThis) {
            thisObject = (This) object;
            thatObject = null;
        } else {
            thisObject = null;
            thatObject = (That) object;
        }
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
        return new FatOr<>(isThis ? thisObject : thatObject, isThis);
    }

    @Override
    public final boolean isThis() {
        return isThis;
    }

    @Override
    public final This getThis() {
        return thisObject;
    }

    @Override
    public final That getThat() {
        return thatObject;
    }
}
