package ir.smmh.mind;

import java.util.function.Supplier;

public interface Property {

    Idea getOrigin();

    String getName();

    Idea getType();

    Supplier<Value> getDefaultValue();

    default String encode() {
        return "has " + getName() + " as " + getType().getName();
    }
}
