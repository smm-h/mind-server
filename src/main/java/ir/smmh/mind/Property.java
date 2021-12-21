package ir.smmh.mind;

import ir.smmh.util.Generator;

public interface Property {

    Idea getOrigin();

    String getName();

    Idea getType();

    Generator<Value> getDefaultValue();

    default String encode() {
        return "has " + getName() + " as " + getType().getName();
    }
}
