package ir.smmh.mind.impl;

import ir.smmh.common.MutableAdapter;
import ir.smmh.common.impl.MutableImpl;
import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
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

    private final ir.smmh.common.Mutable<Immutable> mutableAdapter = new MutableImpl<>() {
        @Override
        public @NotNull Immutable freeze() {
            // TODO freezing a mind
            return new ImmutableMindImpl();
        }
    };

    @Override
    public ir.smmh.common.Mutable<Immutable> getMutableAdapter() {
        return mutableAdapter;
    }
}
