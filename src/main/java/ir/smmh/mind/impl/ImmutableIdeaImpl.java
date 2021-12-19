package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Property;

import java.util.Set;

public final class ImmutableIdeaImpl extends AbstractIdeaImpl implements Idea.Immutable {
    public ImmutableIdeaImpl(String name, Set<Idea> intensions, Set<Property<?>> properties, Set<Property<?>> staticProperties) {
        super(name, intensions, properties, staticProperties);
    }
}
