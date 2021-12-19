package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class MutableIdeaImpl extends AbstractIdeaImpl implements Idea.Mutable {

    private boolean dirty = true;

    public MutableIdeaImpl(String name, Set<Idea> intensions, Set<Property<?>> properties, Set<Property<?>> staticProperties) {
        super(name, intensions, properties, staticProperties);
    }

    @Override
    public @NotNull Immutable freeze() {
        return new ImmutableIdeaImpl(getName(), Collections.unmodifiableSet(intensions), Collections.unmodifiableSet(properties), Collections.unmodifiableSet(staticProperties));
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void taint() {
        dirty = true;
    }

    @Override
    public void onClean() {
        dirty = false;
    }

    @Override
    public void become(Idea idea) {
        if (!intensions.contains(idea)) {
            intensions.add(idea);
            taint();
        }
    }

    @Override
    public void possess(Property<?> property) {
        if (!properties.contains(property)) {
            properties.add(property);
            taint();
        }
    }

    @Override
    public void reify(Property<?> property, String value) {
        if (!staticProperties.contains(property)) {
            properties.remove(property);
            staticProperties.add(property);
            taint();
        }
    }
}
