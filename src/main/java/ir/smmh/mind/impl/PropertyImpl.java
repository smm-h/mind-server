package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Property;

public class PropertyImpl<T> implements Property<T> {

    private final Idea origin;
    private final String name;
    private final Idea type;
    private final T defaultValue;

    public PropertyImpl(Idea origin, String name, Idea type, T defaultValue) {
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
    public T getDefaultValue() {
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
