package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import ir.smmh.mind.Value;
import ir.smmh.storage.Storage;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.Lookup;
import ir.smmh.util.Mutable;
import ir.smmh.util.impl.LookupImpl;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.function.Supplier;

public class MutableMindImpl implements Mind.Mutable, Mutable.Injected {

    private final String name;
    private final @NotNull Lookup.Mutable<Idea.Mutable> ideas;
    //    private final @NotNull Lookup.Multi.Mutable<Property> properties;
    private final ir.smmh.util.Mutable mutableAdapter = new MutableImpl();

    public MutableMindImpl(String name, @Nullable Iterable<Idea.Mutable> ideas) {
        this.name = name;
        this.ideas = new LookupImpl.Mutable<>(ideas);
    }

    // TODO freezing a mind

    public MutableMindImpl(JSONObject object) {
        this.name = object.getString("name");
        Iterable<Idea.Mutable> i = JSONUtil.arrayOfObjects(object, "ideas", new HashSet<>(), o -> new MutableIdeaImpl(this, o));
        this.ideas = new LookupImpl.Mutable<>(i);
    }

    @Override
    public @Nullable Idea.Mutable findIdeaByName(String name) {
        return ideas.find(name);
    }

    @Override
    public Supplier<Value> makeValueGenerator(@NotNull JSONObject source) {
        return () -> Value.of(source, this::findIdeaByName);
    }

    @Override
    public @NotNull Storage getStorage() {
        return storage;
    }

    @Override
    public void onClean() {
        Mutable.super.onClean();
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Idea.Mutable imagine(String name) {
        @Nullable Idea.Mutable idea = ideas.find(name);
        if (idea == null) {
            idea = new MutableIdeaImpl(this, name, new HashSet<>(), new HashSet<>(), new HashSet<>());
            ideas.add(idea);
            taint();
        }
        return idea;
    }

    @Override
    public @NotNull Lookup<Idea.Mutable> getIdeaLookup() {
        return ideas;
    }

    @Override
    public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
        return mutableAdapter;
    }

    @Override
    public @NotNull String serialize() {
        return null;
        // TODO serialize mind
    }
}
