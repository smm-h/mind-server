package ir.smmh.mind.impl;

import ir.smmh.common.MutableAdapter;
import ir.smmh.common.impl.MutableImpl;
import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;

public class MutableMindImpl implements Mind.Mutable, MutableAdapter<Mind.Immutable> {

    private final Map<String, Idea.Mutable> ideas = new HashMap<>();

    @Override
    public @NotNull Idea.Mutable imagine(String name, boolean create) {
        if (!ideas.containsKey(name)) {
            if (create) {
                ideas.put(name, new MutableIdeaImpl(name, new HashSet<>(), new HashSet<>(), new HashSet<>()));
            } else {
                throw new NoSuchElementException("no such idea: " + name);
            }
        }
        return ideas.get(name);
    }

    @Override
    public @Nullable Idea.Mutable find(String name) {
        return null;
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
