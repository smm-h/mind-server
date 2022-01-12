package ir.smmh.mind.impl;

import ir.smmh.mind.Mind;
import ir.smmh.mind.Value;
import ir.smmh.storage.Storage;
import ir.smmh.storage.impl.StorageImpl;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.Lookup;
import ir.smmh.util.Mutable;
import ir.smmh.util.Serializable;
import ir.smmh.util.impl.LookupImpl;
import ir.smmh.util.impl.MutableHashSet;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.function.Supplier;

public class MutableMindImpl implements Mind.Mutable, Mutable.Injected, Serializable.JSON {

    private final String name;
    private final @NotNull Lookup.Mutable<MutableIdeaImpl> ideas;
    private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);
    private final Storage storage = new StorageImpl("minds");

    public MutableMindImpl(String name, @Nullable Iterable<MutableIdeaImpl> ideas) {
        this.name = name;
        this.ideas = new LookupImpl.Mutable<>(ideas);
        setup();
    }

    public MutableMindImpl(JSONObject object) throws JSONException {
        this.name = object.getString("name");
        this.ideas = new LookupImpl.Mutable<>(JSONUtil.arrayOfObjects(object, "ideas", new HashSet<>(), o -> {
            try {
                return new MutableIdeaImpl(this, o);
            } catch (JSONException e) {
                e.printStackTrace();
                // TODO FIXME
                return null;
            }
        }));
        setup();
    }

    @Override
    public @Nullable MutableIdeaImpl findIdeaByName(String name) {
        return ideas.find(name);
    }

    private void setup() {
        setupStored();
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
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull MutableIdeaImpl imagine(@NotNull String name) {
        @Nullable MutableIdeaImpl idea = ideas.find(name);
        if (idea == null) {
            idea = new MutableIdeaImpl(this, name, new MutableHashSet<>(), new HashSet<>(), new HashSet<>());
            ideas.add(idea);
            taint();
        }
        return idea;
    }

    @Override
    public @NotNull Lookup.Mutable<MutableIdeaImpl> getIdeaLookup() {
        return ideas;
    }

    @Override
    public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
        return injectedMutable;
    }

    @Override
    public @NotNull JSONObject serializeJSON() throws JSONException {
        clean();
        JSONObject object = new JSONObject();
        try {
            JSONArray ideas = new JSONArray();
            for (String ideaName : this.ideas) {
                MutableIdeaImpl idea = this.ideas.find(ideaName);
                assert idea != null;
                ideas.put(idea.serializeJSON());
            }
            object.put("name", name);
            object.put("ideas", ideas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
