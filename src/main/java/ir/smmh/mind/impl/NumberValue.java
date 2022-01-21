package ir.smmh.mind.impl;

import ir.smmh.mind.Value;

public class NumberValue implements Value.Number {
    private final java.lang.Number value;

    public NumberValue(java.lang.Number value) {
        super();
        this.value = value;
    }

    @Override
    public final java.lang.Number getValue() {
        return value;
    }
}
