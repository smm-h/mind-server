package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Instance;
import ir.smmh.mind.Mind;
import ir.smmh.mind.Property;
import ir.smmh.util.Comprehension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractIdeaImpl implements Idea {

    protected final Mind mind;
    protected final String name;
    protected final Set<Idea> intensions;
    protected final Map<String, Property> properties;
    protected final Map<String, Property> staticProperties;

    private static final Comprehension.Map<Property, String, Property> mapper = p -> new AbstractMap.SimpleEntry<>(p.getName(), p);

    public AbstractIdeaImpl(Mind mind, String name, Set<Idea> intensions, Iterable<Property> properties, Iterable<Property> staticProperties) {
        this.mind = mind;
        this.name = name;
        this.intensions = intensions;
        this.properties = mapper.comprehend(properties);
        this.staticProperties = mapper.comprehend(staticProperties);
    }

    @Override
    public Mind getMind() {
        return mind;
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
    public Set<Property> getDirectProperties() {
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
