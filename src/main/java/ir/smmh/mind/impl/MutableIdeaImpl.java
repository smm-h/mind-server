package ir.smmh.mind.impl;

import ir.smmh.mind.*;
import ir.smmh.storage.Storage;
import ir.smmh.storage.impl.StorageImpl;
import ir.smmh.util.Comprehension;
import ir.smmh.util.JSONUtil;
import ir.smmh.util.Mutable;
import ir.smmh.util.Serializable;
import ir.smmh.util.impl.MutableHashSet;
import ir.smmh.util.impl.MutableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Map.entry;

public class MutableIdeaImpl implements Idea.Mutable, Mutable.Injected, Serializable.JSON {

    private static final Comprehension.Map<PropertyImpl, String, PropertyImpl> c = p -> entry(p.getName(), p);
    private static final Comprehension.Map<StaticPropertyImpl, String, StaticPropertyImpl> sc = p -> entry(p.getName(), p);
    private final Mind mind;
    private final String name;
    private final Set<String> intensions;
    private final Map<String, PropertyImpl> properties;
    private final Map<String, StaticPropertyImpl> staticProperties;
    private final ir.smmh.util.Mutable injectedMutable = new MutableImpl(this);
    private final Storage storage;
    private java.util.Set<Idea> intensionsCache;

    public MutableIdeaImpl(@NotNull Mind mind, @NotNull String name, @NotNull Set<String> intensions, @NotNull Iterable<PropertyImpl> properties, @NotNull Iterable<StaticPropertyImpl> staticProperties) {
        this.mind = mind;
        this.name = name;
        this.intensions = intensions;
        this.properties = c.comprehend(properties);
        this.staticProperties = sc.comprehend(staticProperties);
        this.storage = new StorageImpl(mind.getName());
        setup();
    }

    public MutableIdeaImpl(Mind mind, JSONObject object) throws JSONException {
        this.mind = mind;
        this.name = object.getString("name");
        this.intensions = JSONUtil.arrayOfStrings(object, "intensions", new MutableHashSet<>());
        this.properties = c.comprehend(JSONUtil.arrayOfObjects(object, "properties", new HashSet<>(), o -> new PropertyImpl(this, o)));
        this.staticProperties = sc.comprehend(JSONUtil.arrayOfObjects(object, "static-properties", new HashSet<>(), o -> new StaticPropertyImpl(this, o)));
        this.storage = new StorageImpl(mind.getName());
        setup();
    }

    @Override
    public @NotNull JSONObject serializeJSON() throws JSONException {
        JSONObject object = new JSONObject();
        try {
            object.put("name", name);
            object.put("intensions", intensions);
            object.put("properties", properties.values()); // ((Comprehension.List<String, PropertyImpl>) properties::get).comprehend(properties.keySet()));
            object.put("static-properties", staticProperties.values()); // ((Comprehension.List<String, PropertyImpl>) staticProperties::get).comprehend(staticProperties.keySet()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private void setup() {
        setupStored();
        intensions.addOnCleanListener(() -> intensionsCache = ((Comprehension.Set<String, Idea>) mind::findIdeaByName).comprehend(intensions));
    }

    @Override
    public Mind getMind() {
        return mind;
    }

    @Override
    public boolean hasDirectly(@NotNull String propertyName) {
        return properties.containsKey(propertyName);
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable java.util.Set<Idea> getDirectIntensions() {
        intensions.clean();
        return intensionsCache;
    }

    @Override
    public java.util.Set<Property> getDirectProperties() {
        return new HashSet<>(properties.values());
        // TODO optimize with caching
    }

    @Override
    public @NotNull Instance instantiate() {
        return new InstanceImpl(this);
    }

    @NotNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public void become(String ideaName) {
        if (!intensions.contains(ideaName)) {
            intensions.add(ideaName);
            taint();
        }
    }

    @Override
    public PropertyImpl possess(String name, String type, Supplier<Value> defaultValue) {
        if (!properties.containsKey(name)) {
            PropertyImpl property = new PropertyImpl(this, name, type, defaultValue);
            properties.put(name, property);
            taint();
        }
        return properties.get(name);
    }

    @Override
    public StaticPropertyImpl reify(String name, String type, Value value) {
        if (!staticProperties.containsKey(name)) {
            StaticPropertyImpl property = new StaticPropertyImpl(this, name, type, value);
            staticProperties.put(name, property);
            taint();
        }
        return staticProperties.get(name);
    }

    @Override
    public @NotNull ir.smmh.util.Mutable getInjectedMutable() {
        return injectedMutable;
    }

    @Override
    public @NotNull Storage getStorage() {
        return storage;
    }
}
