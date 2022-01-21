package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import ir.smmh.mind.Value;
import ir.smmh.nile.verbs.CanSerialize;
import ir.smmh.storage.Storage;
import ir.smmh.storage.impl.StorageImpl;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.Map;
import ir.smmh.util.Mutable;
import ir.smmh.util.impl.MapImpl;
import ir.smmh.util.impl.MutableCollectionImpl;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "StaticMethodOnlyUsedInOneClass", "ThrowsRuntimeException"})
public final class MutableMindImpl implements Mind.Mutable, Mutable.WithListeners.Injected, CanSerialize.JSON {

    private final String name;
    private final @NotNull Map.SingleValue.Mutable<String, MutableIdeaImpl> ideas = new MapImpl.SingleValue.Mutable<>();
    private final ir.smmh.util.Mutable.WithListeners injectedMutable = MutableImpl.blank();
    private final Storage storage = StorageImpl.of("minds");

    private MutableMindImpl(String name, @Nullable Iterable<MutableIdeaImpl> ideas) {
        super();
        this.name = name;
        if (ideas != null) {
            for (MutableIdeaImpl idea : ideas) {
                this.ideas.setAtPlace(idea.getName(), idea);
            }
        }
        setup();
    }

    private MutableMindImpl(JSONObject object) throws JSONException {
        super();
        name = object.getString("name");
        for (MutableIdeaImpl idea : JSONUtil.arrayOfObjects(object, "ideas", new HashSet<>(8), o -> {
            try {
                return new MutableIdeaImpl(this, o);
            } catch (JSONException e) {
                e.printStackTrace();
                // TODO FIXME
                return null;
            }
        })) {
            ideas.setAtPlace(idea.getName(), idea);
        }
        setup();
    }

    @NotNull
    public static Mind.Mutable createBlank(String name, @Nullable Iterable<MutableIdeaImpl> ideas) {
        return new MutableMindImpl(name, ideas);
    }

    @Nullable
    public static Mind.Mutable parse(String identifier, String serialization) {
        try {
            return parse(identifier, JSONUtil.parse(serialization));
        } catch (JSONException e) {
            return null;
        }
    }

    @Nullable
    public static Mind.Mutable parse(String identifier, JSONObject object) {
        try {
            return new MutableMindImpl(object);
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public @NotNull Iterable<String> overIdeaNames() {
        return ideas.overKeys();
    }

    @Override
    public @NotNull Iterable<MutableIdeaImpl> overIdeas() {
        return ideas.overValues();
    }

    @Override
    public @Nullable Idea.Mutable findIdeaByName(String ideaName) {
        return ideas.getAtPlace(ideaName);
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
    public @NotNull Idea.Mutable imagine(@NotNull String ideaName) {
        @Nullable MutableIdeaImpl idea = ideas.getAtPlace(ideaName);
        if (idea == null) {
            preMutate();
            idea = new MutableIdeaImpl(this, ideaName, MutableCollectionImpl.of(new HashSet<>()), new HashSet<>(8),
                    new HashSet<>(8));
            ideas.setAtPlace(idea.getName(), idea);
            postMutate();
        }
        return idea;
    }

    @Override
    public @NotNull Function<String, MutableIdeaImpl> getIdeaLookup() {
        return ideas;
    }

    @Override
    public @NotNull ir.smmh.util.Mutable.WithListeners getInjectedMutable() {
        return injectedMutable;
    }

    @Override
    public @NotNull JSONObject serializeJSON() throws JSONException {
        JSONObject object = new JSONObject();
        if (clean()) {
            try {
                JSONArray array = new JSONArray();
                for (MutableIdeaImpl idea : ideas.overValues()) {
                    array.put(idea.serializeJSON());
                }
                object.put("name", name);
                object.put("ideas", array);
            } catch (JSONException ignored) {
            }
        }
        return object;
    }
}
