package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Property;
import ir.smmh.mind.Value;

import java.util.function.Supplier;

public class PropertyImpl implements Property {

    private final Idea origin;
    private final String name;
    private final Idea type;
    private final Supplier<Value> defaultValue;

    public PropertyImpl(Idea origin, String name, Idea type, Supplier<Value> defaultValue) {
        this.origin = origin;
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    @Override
    public Idea getOrigin() {
        return origin;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Idea getType() {
        return type;
    }

    @Override
    public Supplier<Value> getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertyImpl)) return false;

        PropertyImpl property = (PropertyImpl) o;

        if (!origin.equals(property.origin)) return false;
        if (!name.equals(property.name)) return false;
        return type.equals(property.type);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
