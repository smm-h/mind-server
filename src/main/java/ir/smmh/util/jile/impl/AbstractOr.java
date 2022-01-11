package ir.smmh.util.jile.impl;

import ir.smmh.util.jile.Or;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractOr<This, That> implements Or<This, That> {

    @Override
    public int hashCode() {
        if (isThis())
            return getThis().hashCode();
        else
            return getThat().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Or) {
            Or<?, ?> other = (Or<?, ?>) object;
            if (isThis())
                return getThis().equals(other.getThis()) || getThis().equals(other.getThat());
            else
                return getThat().equals(other.getThat()) || getThat().equals(other.getThis());
        } else {
            if (isThis())
                return getThis().equals(object);
            else
                return getThat().equals(object);
        }
    }

    @NotNull
    @Override
    public String toString() {
        if (isThis())
            return getThis().toString();
        else
            return getThat().toString();
    }
}
