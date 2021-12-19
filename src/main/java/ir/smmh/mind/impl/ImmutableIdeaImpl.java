package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import ir.smmh.mind.Property;

import java.util.Set;

public final class ImmutableIdeaImpl extends AbstractIdeaImpl implements Idea.Immutable {
    public ImmutableIdeaImpl(Mind mind, String name, Set<Idea> intensions, Iterable<Property> properties, Iterable<Property> staticProperties) {
        super(mind, name, intensions, properties, staticProperties);
    }
}
