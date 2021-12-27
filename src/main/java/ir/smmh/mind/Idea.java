package ir.smmh.mind;

import ir.smmh.storage.Stored;
import ir.smmh.util.Named;
import ir.smmh.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Set;
import java.util.function.Supplier;

public interface Idea extends Named {

    Mind getMind();

    default boolean is(@Nullable Idea idea) {

        // If it is null, I am not it
        if (idea == null)
            return false;

        // If it is me, I am it
        if (idea == this)
            return true;

        // If I have no direct intensions, I am not it
        final Set<Idea> d = getDirectIntensions();
        if (d == null)
            return false;

        // If it is in my direct intensions, I am it
        if (d.contains(idea))
            return true;

        // If any of my direct intensions are it, I am it
        for (Idea i : d) {
            if (i.is(idea)) {
                return true;
            }
        }

        // If none of my intensions are it, I am not it
        return false;
    }

    default boolean has(@Nullable Property property) {
        return property != null && is(property.getOrigin());
    }

    default boolean is(@NotNull String idea) {
        return is(getMind().findIdeaByName(idea));
    }

    boolean hasDirectly(@NotNull String propertyName);

    default boolean has(@Nullable String propertyName) {

        // If its name is null, I do not have it
        if (propertyName == null)
            return false;

        // If I directly have it, I have it
        if (hasDirectly(propertyName))
            return true;

        // If I have no direct intensions, I do not have it
        final Set<Idea> d = getDirectIntensions();
        if (d == null)
            return false;

        // If any of my direct intensions have it, I have it
        for (Idea i : d) {
            if (i.has(propertyName)) {
                return true;
            }
        }

        // If none of my intensions are idea, I am not it
        return false;
    }

    @Nullable
    Set<Idea> getDirectIntensions();

    @Nullable
    default Set<Idea> getAllIntensions() {
        final Set<Idea> set = getDirectIntensions();
        if (set == null) {
            return null;
        } else {
            final Set<Idea> d = getDirectIntensions();
            if (d != null) {
                for (Idea intension : d) {
                    final Set<Idea> all = intension.getAllIntensions();
                    if (all != null) {
                        set.addAll(all);
                    }
                }
            }
        }
        return set;
    }

    @Nullable
    Set<Property> getDirectProperties();

    @Nullable
    default Set<Property> getAllProperties() {
        final Set<Property> set = getDirectProperties();
        if (set == null) {
            return null;
        } else {
            final Set<Idea> allIntensions = getAllIntensions();
            if (allIntensions != null) {
                for (Idea intension : allIntensions) {
                    final Set<Property> allProperties = intension.getAllProperties();
                    if (allProperties != null) {
                        set.addAll(allProperties);
                    }
                }
            }
        }
        return set;
    }

    default boolean canBeDeserialized(JSONObject serialization) {
        final Set<Idea> d = getDirectIntensions();
        if (d != null) {
            for (Idea idea : d) {
                if (!serialization.has(idea.getName())) {
                    return false;
                } else if (!idea.canBeDeserialized(serialization)) {
                    return false;
                }
            }
        }
        final Set<Property> p = getDirectProperties();
        if (p != null) {
            for (Property property : p) {
                if (!serialization.has(property.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    default Instance deserialize(JSONObject serialization) {
        Instance instance = instantiate();
        final Set<Idea> d = getDirectIntensions();
        if (d != null) {
            for (Idea idea : d) {
                instance.setLink(idea, idea.deserialize(serialization.getJSONObject(idea.getName())));
            }
        }
        final Set<Property> p = getDirectProperties();
        if (p != null) {
            for (Property property : p) {
                instance.set(property, Value.of(serialization.getJSONObject(property.getName()), getMind()::findIdeaByName));
            }
        }
        return instance;
    }

    @NotNull
    Instance instantiate();

    @Nullable
    default Instance instantiate(JSONObject serialization) {
        if (canBeDeserialized(serialization)) {
            return deserialize(serialization);
        } else {
            return null;
        }
    }

    interface Mutable extends Idea, Stored {

        default void become(Idea idea) {
            become(idea.getName());
        }

        void become(String ideaName);

        default Property possess(String name, String type) {
            return possess(name, type, null);
        }

        Property possess(String name, String type, Supplier<Value> defaultValue);

        Property reify(String name, String type, Value value);
    }

    interface Immutable extends Idea {
    }

    default String encode() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName());
        final Set<Idea> d = getDirectIntensions();
        final Set<Property> p = getDirectProperties();
        if ((d == null || d.isEmpty()) && (p == null || p.isEmpty())) {
            builder.append(" {}");
        } else {
            builder.append(" {\n");
            if (d != null) {
                for (Idea idea : d) {
                    builder.append(StringUtil.tabIn("is " + idea.encode()));
                    builder.append('\n');
                }
            }
            if (p != null) {
                for (Property property : p) {
                    builder.append(StringUtil.tabIn(property.encode()));
                    builder.append('\n');
                }
            }
            builder.append('}');
        }
        return builder.toString();
    }
}
