package ir.smmh.mind.impl;

import ir.smmh.common.MutableAdapter;
import ir.smmh.common.impl.MutableImpl;
import ir.smmh.mind.Idea;
import ir.smmh.mind.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class MutableIdeaImpl extends AbstractIdeaImpl implements Idea.Mutable, MutableAdapter<Idea.Immutable> {

    public MutableIdeaImpl(String name, Set<Idea> intensions, Set<Property<?>> properties, Set<Property<?>> staticProperties) {
        super(name, intensions, properties, staticProperties);
    }

    @Override
    public void become(Idea idea) {
        if (!intensions.contains(idea)) {
            intensions.add(idea);
            taint();
        }
    }

    @Override
    public <T> Property<T> possess(String name, Idea type, T defaultValue) {
        if (!properties.containsKey(name)) {
            properties.put(name, new PropertyImpl<>(this, name, type, defaultValue));
            taint();
        }
        return (Property<T>) properties.get(name);
    }

    @Override
    public <T> Property<T> reify(String name, Idea type, T value) {

        if (!staticProperties.contains(property)) {
            properties.remove(property);
            staticProperties.add(property);
            taint();
        }
    }

    private final ir.smmh.common.Mutable<Immutable> mutableAdapter = new MutableImpl<>() {
        @Override
        public @NotNull Immutable freeze() {
            return new ImmutableIdeaImpl(getName(), Collections.unmodifiableSet(intensions), Collections.unmodifiableSet(properties), Collections.unmodifiableSet(staticProperties));
        }
    };

    @Override
    public ir.smmh.common.Mutable<Immutable> getMutableAdapter() {
        return mutableAdapter;
    }
}
