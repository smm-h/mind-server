package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import ir.smmh.mind.Property;
import ir.smmh.util.MutableAdapter;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;

public class MutableMindImpl extends AbstractMindImpl<Idea.Mutable> implements Mind.Mutable, MutableAdapter<Mind.Immutable> {

    public MutableMindImpl() {
        super();
    }

    public MutableMindImpl(Map<String, Idea.Mutable> ideas) {
        super(ideas);
    }

    @Override
    public @NotNull Idea.Mutable imagine(String name) {
        Idea.Mutable idea = find(name);
        if (idea == null) {
            idea = new MutableIdeaImpl(this, name, new HashSet<>(), new HashSet<>(), new HashSet<>());
            ideas.put(name, idea);
            taint();
        }
        return idea;
    }

    void addProperty(Property property) {
        final String name = property.getName();
        if (!properties.containsKey(name)) {
            properties.put(name, new HashSet<>());
        }
        properties.get(name).add(property);
    }

    private final ir.smmh.util.Mutable<Immutable> mutableAdapter = new MutableImpl<>() {
        @Override
        public @NotNull Immutable freeze() {
            // TODO freezing a mind
            return new ImmutableMindImpl();
        }
    };

    @Override
    public ir.smmh.util.Mutable<Immutable> getMutableAdapter() {
        return mutableAdapter;
    }
}
