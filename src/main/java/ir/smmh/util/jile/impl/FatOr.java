package ir.smmh.util.jile.impl;

/**
 * Takes double the size in memory, but is checked
 */
public class FatOr<This, That> extends AbstractOr<This, That> {

    private final This thisObject;
    private final That thatObject;
    private final boolean isThis;

    @SuppressWarnings("unchecked")
    private FatOr(Object object, boolean isThis) {
        this.isThis = isThis;
        if (isThis) {
            thisObject = (This) object;
            thatObject = null;
        } else {
            thisObject = null;
            thatObject = (That) object;
        }
    }

    @Override
    public FatOr<This, That> makeThis(This object) {
        return new FatOr<>(object, true);
    }

    @Override
    public FatOr<This, That> makeThat(That object) {
        return new FatOr<>(object, false);
    }

    @Override
    public boolean isThis() {
        return isThis;
    }

    @Override
    public boolean isThat() {
        return !isThis;
    }

    @Override
    public This getThis() {
        return thisObject;
    }

    @Override
    public That getThat() {
        return thatObject;
    }
}
