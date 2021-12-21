package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import ir.smmh.mind.Property;
import ir.smmh.mind.Value;
import ir.smmh.util.Generator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMindImpl<I extends Idea> implements Mind {

    protected final Map<String, I> ideas;
    protected final Map<String, Set<Property>> properties;

    protected AbstractMindImpl() {
        this(new HashMap<>());
    }

    protected AbstractMindImpl(@NotNull Map<String, I> ideas) {
        this.ideas = ideas;
        properties = new HashMap<>();
        for (final Idea idea : ideas.values()) {
            final Iterable<Property> p = idea.getDirectProperties();
            if (p != null) {
                for (final Property property : p) {
                    final String name = property.getName();
                    if (!properties.containsKey(name)) {
                        properties.put(name, new HashSet<>());
                    }
                    properties.get(name).add(property);
                }
            }
        }
    }

    @Override
    public @Nullable I find(String name) {
        return ideas.get(name);
    }

    @Override
    @Nullable
    public Set<Property> findProperties(String name) {
        return properties.get(name);
    }

    @Override
    public Generator<Value> makeValueGenerator(@NotNull JSONObject source) {
        return () -> valueOf(source);
    }
}
