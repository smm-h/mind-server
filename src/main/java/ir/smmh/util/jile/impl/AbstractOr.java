package ir.smmh.util.jile.impl;

import ir.smmh.util.jile.Or;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractOr<This, That> implements Or<This, That> {

    @Override
    public final int hashCode() {
        return Objects.hashCode(getObject());
    }

    @Override
    public final boolean equals(Object object) {
        return Objects.equals(getObject(), object instanceof Or ? ((Or<?, ?>) object).getObject() : object);
    }

    @NotNull
    @Override
    public final String toString() {
        return Objects.toString(getObject());
    }
}
