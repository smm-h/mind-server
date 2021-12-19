package ir.smmh.mind;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Set;

public interface Idea {

    Mind getMind();

    String getName();

    default boolean is(Idea idea) {

        // If idea is me, I am it
        if (idea == this)
            return true;

        // If I have no direct intensions, I am not it
        final Set<Idea> d = getDirectIntensions();
        if (d == null)
            return false;

        // If idea is in my direct intensions, I am it
        if (d.contains(idea))
            return true;

        // If any of my intensions are idea, I am it
        for (Idea i : d) {
            if (i.is(idea)) {
                return true;
            }
        }

        // If none of my intensions are idea, I am not it
        return false;
    }

    default boolean has(Property property) {
        return is(property.getOrigin());
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
                instance.set(property, getMind().valueOf(serialization.getJSONObject(property.getName())));
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

    interface Mutable extends Idea, ir.smmh.common.Mutable<Immutable> {

        void become(Idea idea);

        default Property possess(String name, Idea type) {
            return possess(name, type, null);
        }

        Property possess(String name, Idea type, Value defaultValue);

        Property reify(String name, Idea type, Value value);
    }

    interface Immutable extends Idea {
    }
}
