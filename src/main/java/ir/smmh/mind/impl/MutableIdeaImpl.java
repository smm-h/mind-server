package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import ir.smmh.mind.Property;
import ir.smmh.mind.Value;
import ir.smmh.util.Generator;
import ir.smmh.util.MutableAdapter;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class MutableIdeaImpl extends AbstractIdeaImpl implements Idea.Mutable, MutableAdapter<Idea.Immutable> {

    public MutableIdeaImpl(Mind mind, String name, Set<Idea> intensions, Iterable<Property> properties, Iterable<Property> staticProperties) {
        super(mind, name, intensions, properties, staticProperties);
    }

    @Override
    public void become(Idea idea) {
        if (!intensions.contains(idea)) {
            intensions.add(idea);
            taint();
        }
    }

    @Override
    public Property possess(String name, Idea type, Generator<Value> defaultValue) {
        if (!properties.containsKey(name)) {
            Property property = new PropertyImpl(this, name, type, defaultValue);
            ((MutableMindImpl) mind).addProperty(property);
            properties.put(name, property);
            taint();
        }
        return properties.get(name);
    }

    @Override
    public Property reify(String name, Idea type, Value value) {
        if (!staticProperties.containsKey(name)) {
            Property property = new PropertyImpl(this, name, type, () -> value);
            ((MutableMindImpl) mind).addProperty(property);
            staticProperties.put(name, property);
            taint();
        }
        return staticProperties.get(name);
    }

    private final ir.smmh.util.Mutable<Immutable> mutableAdapter = new MutableImpl<>() {
        @Override
        public @NotNull Immutable freeze() {
            return new ImmutableIdeaImpl(mind, name, Collections.unmodifiableSet(intensions), properties.values(), staticProperties.values());
        }
    };

    @Override
    public ir.smmh.util.Mutable<Immutable> getMutableAdapter() {
        return mutableAdapter;
    }
}
