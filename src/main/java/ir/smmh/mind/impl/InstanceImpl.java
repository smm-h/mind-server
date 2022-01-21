package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Instance;
import ir.smmh.mind.Property;
import ir.smmh.mind.Value;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InstanceImpl implements Instance {

    private final @NotNull Idea type;
    private final Map<Property, Value> values = new HashMap<>();
    private final Map<Idea, Instance> intensions = new HashMap<>();

    public InstanceImpl(@NotNull Idea type) {
        super();
        this.type = type;
    }

    @Override
    public final @NotNull Idea getType() {
        return type;
    }

    @Override
    public final boolean has(Property property) {
        return values.containsKey(property);
    }

    @Override
    public final void set(Property property, Value value) {
        values.put(property, value);
    }

    @Override
    public final Value get(Property property) {
        return values.get(property);
    }

    @Override
    public final boolean is(Idea idea) {
        return intensions.containsKey(idea);
    }

    @Override
    public final void setLink(Idea idea, Instance instance) {
        intensions.put(idea, instance);
    }

    @Override
    public final Instance getLink(Idea idea) {
        return intensions.get(idea);
    }
}
