package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Instance;
import ir.smmh.mind.Property;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InstanceImpl implements Instance {

    private final @NotNull Idea type;
    private final Map<Property<?>, Object> values = new HashMap<>();
    private final Map<Idea, Instance> intensions = new HashMap<>();

    public InstanceImpl(@NotNull Idea type) {
        this.type = type;
    }

    @Override
    public @NotNull Idea getType() {
        return type;
    }

    @Override
    public <T> boolean has(Property<T> property) {
        return values.containsKey(property);
    }

    @Override
    public <T> void set(Property<T> property, Object value) {
        values.put(property, value);
    }

    @Override
    public <T> T get(Property<T> property) {
        try {
            return (T) values.get(property);
        }catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public boolean is(Idea idea) {
        return intensions.containsKey(idea);
    }

    @Override
    public void setLink(Idea idea, Instance instance) {
        intensions.put(idea, instance);
    }

    @Override
    public Instance getLink(Idea idea) {
        return intensions.get(idea);
    }
}
