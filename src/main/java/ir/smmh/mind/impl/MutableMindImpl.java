package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import ir.smmh.mind.Value;
import ir.smmh.storage.Storage;
import ir.smmh.storage.impl.StorageImpl;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.Map;
import ir.smmh.util.Mutable;
import ir.smmh.util.Serializable;
import ir.smmh.util.impl.MapImpl;
import ir.smmh.util.impl.MutableHashSet;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

public class MutableMindImpl implements Mind.Mutable, Mutable.Injected, Serializable.JSON {

    private final String name;
    private final @NotNull Map.SingleValue.Mutable<String, MutableIdeaImpl> ideas = new MapImpl.SingleValue.Mutable<>();
    private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);
    private final Storage storage = new StorageImpl("minds");

    public MutableMindImpl(String name, @Nullable Iterable<MutableIdeaImpl> ideas) {
        this.name = name;
        if (ideas != null) {
            for (MutableIdeaImpl idea : ideas) {
                this.ideas.place(idea.getName(), idea);
            }
        }
        setup();
    }

    public MutableMindImpl(JSONObject object) throws JSONException {
        this.name = object.getString("name");
        for (MutableIdeaImpl idea : JSONUtil.arrayOfObjects(object, "ideas", new HashSet<>(), o -> {
            try {
                return new MutableIdeaImpl(this, o);
            } catch (JSONException e) {
                e.printStackTrace();
                // TODO FIXME
                return null;
            }
        })) {
            this.ideas.place(idea.getName(), idea);
        }
        setup();
    }

    @Override
    public Iterable<String> overIdeaNames() {
        return ideas.overKeys();
    }

    @Override
    public Iterable<Idea> overIdeas() {
        return null;
    }

    @Override
    public @Nullable MutableIdeaImpl findIdeaByName(String name) {
        return ideas.get(name);
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
        @Nullable MutableIdeaImpl idea = ideas.get(name);
        if (idea == null) {
            idea = new MutableIdeaImpl(this, name, new MutableHashSet<>(), new HashSet<>(), new HashSet<>());
            ideas.place(idea.getName(), idea);
            postMutate();
        }
        return idea;
    }

    @Override
    public @NotNull Function<String, MutableIdeaImpl> getIdeaLookup() {
        return ideas;
    }

    @Override
    public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
        return injectedMutable;
    }

    @Override
    public @NotNull JSONObject serializeJSON() throws JSONException {
        JSONObject object = new JSONObject();
        if (clean()) {
            try {
                JSONArray ideas = new JSONArray();
                for (MutableIdeaImpl idea : this.ideas.overValues()) {
                    ideas.put(idea.serializeJSON());
                }
                object.put("name", name);
                object.put("ideas", ideas);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
}
