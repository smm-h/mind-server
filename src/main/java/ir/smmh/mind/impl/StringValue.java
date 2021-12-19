package ir.smmh.mind.impl;

import ir.smmh.mind.Value;

public class StringValue implements Value.String {
    private final java.lang.String value;

    public StringValue(java.lang.String value) {
        this.value = value;
    }

    @Override
    public java.lang.String getValue() {
        return value;
    }
}
