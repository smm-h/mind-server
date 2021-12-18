package ir.smmh.mind.impl;

import ir.smmh.mind.Instance;
import ir.smmh.mind.Idea;
import ir.smmh.mind.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class IdeaImpl implements Idea {
    
    private final String name;
    private final Set<Idea> intensions;
    private final Set<Property> properties;

    public IdeaImpl(String name, Set<Idea> intensions, Set<Property> properties) {
        this.name = name;
        this.intensions = intensions;
        this.properties = properties;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable Set<Idea> getDirectIntensions() {
        return intensions;
    }

    @Override
    public @Nullable Set<Idea> getDirectExtensions() {
        return null;
    }

    @Override
    public Set<Property> getDirectProperties() {
        return properties;
    }

    @Override
    public @NotNull Instance createBlank() {
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
