package ir.smmh.util.jile.impl;

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

    public static <This, That> SlimOr<This, That> makeThis(This object) {
        return new SlimOr<>(object, true);
    }

    public static <This, That> SlimOr<This, That> makeThat(That object) {
        return new SlimOr<>(object, false);
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
