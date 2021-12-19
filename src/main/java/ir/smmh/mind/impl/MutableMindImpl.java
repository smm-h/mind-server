package ir.smmh.mind.impl;

import ir.smmh.common.impl.MutableImpl;
import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;

public class MutableMindImpl extends MutableImpl<Mind.Immutable> implements Mind.Mutable {

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
    public @NotNull Immutable freeze() {
        return null;
    }

    @Override
    public void onClean() {
        super.onClean();
    }
}
