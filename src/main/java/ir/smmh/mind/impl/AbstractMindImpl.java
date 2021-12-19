package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMindImpl<I extends Idea> implements Mind {

    protected final Map<String, I> ideas;

    protected AbstractMindImpl() {
        this(new HashMap<>());
    }

    protected AbstractMindImpl(@NotNull Map<String, I> ideas) {
        this.ideas = ideas;
    }

    @Override
    public @Nullable I find(String name) {
        return ideas.get(name);
    }
}
