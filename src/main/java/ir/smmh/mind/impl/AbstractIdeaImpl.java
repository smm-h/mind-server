package ir.smmh.mind.impl;

import ir.smmh.common.ForEach;
import ir.smmh.mind.Idea;
import ir.smmh.mind.Instance;
import ir.smmh.mind.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractIdeaImpl implements Idea {

    private final String name;
    protected final Set<Idea> intensions;
    protected final Map<String, Property<?>> properties;
    protected final Map<String, Property<?>> staticProperties;

    private static final ForEach.Mappable<Property<?>, String, Property<?>> mapper = p -> new AbstractMap.SimpleEntry<>(p.getName(), p);

    public AbstractIdeaImpl(String name, Set<Idea> intensions, Iterable<Property<?>> properties, Iterable<Property<?>> staticProperties) {
        this.name = name;
        this.intensions = intensions;
        this.properties = mapper.toMap(properties);
        this.staticProperties = mapper.toMap(staticProperties);
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
        return new HashSet<>(properties.values());
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
