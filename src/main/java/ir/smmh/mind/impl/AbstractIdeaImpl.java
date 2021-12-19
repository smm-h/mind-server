package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Instance;
import ir.smmh.mind.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class AbstractIdeaImpl implements Idea {

    private final String name;
    protected final Set<Idea> intensions;
    protected final Set<Property<?>> properties;
    protected final Set<Property<?>> staticProperties;

    public AbstractIdeaImpl(String name, Set<Idea> intensions, Set<Property<?>> properties, Set<Property<?>> staticProperties) {
        this.name = name;
        this.intensions = intensions;
        this.properties = properties;
        this.staticProperties = staticProperties;
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
    public Set<Property<?>> getDirectProperties() {
        return properties;
    }

    @Override
    public @NotNull Instance instantiate() {
        return new InstanceImpl(this);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
