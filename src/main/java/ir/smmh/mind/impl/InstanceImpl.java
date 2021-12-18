package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Instance;
import ir.smmh.mind.Property;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InstanceImpl implements Instance {

    private final @NotNull Idea type;
    private final Map<Property, Integer> values = new HashMap<>();
    private final Map<Idea, Instance> intensions = new HashMap<>();

    public InstanceImpl(@NotNull Idea type) {
        this.type = type;
    }

    @Override
    public @NotNull Idea getType() {
        return type;
    }

    @Override
    public boolean has(Property property) {
        return values.containsKey(property);
    }

    @Override
    public void set(Property property, int value) {
        values.put(property, value);
    }

    @Override
    public int get(Property property) {
        return values.get(property);
    }

    @Override
    public boolean is(Idea idea) {
        return intensions.containsKey(idea);
    }

    @Override
    public void set(Idea idea, Instance instance) {
        intensions.put(idea, instance);
    }

    @Override
    public Instance get(Idea idea) {
        return intensions.get(idea);
    }
}
