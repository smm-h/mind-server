package ir.smmh.mind;

import ir.smmh.mind.impl.NumberValue;
import ir.smmh.mind.impl.StringValue;
import ir.smmh.util.Generator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Set;

public interface Mind {
    /**
     * Finds and returns an idea with a given name.
     *
     * @param name The name of the idea
     * @return The idea in this mind with that name
     */
    @Nullable
    Idea find(String name);

    /**
     * Returns a set of all the properties in this mind with a given name
     *
     * @param name The name of a property
     * @return A set of properties with that name
     */
    @Nullable
    Set<Property> findProperties(String name);

    default Value valueOf(JSONObject object) {
        final String kind = object.getString("~");
        switch (kind) {
            case "number":
                return new NumberValue(object.getNumber("value"));
            case "string":
                return new StringValue(object.getString("value"));
            default:
                final Idea idea = find(kind);
                if (idea == null) {
                    System.err.println("no such kind: " + kind);
                    return null;
                }
                return idea.deserialize(object);
        }
    }

    /**
     * A mutable mind is an interface that allows you to imagine mutable
     * ideas, mutate them, and once they are coherent, freeze it to get
     * an immutable mind.
     */
    interface Mutable extends Mind, ir.smmh.util.Mutable<Immutable> {
        /**
         * Finds and returns an idea with a given name. It creates the
         * idea if none with that name exists.
         *
         * @param name The name of an idea
         * @return A mutable idea in this mind with that name
         */
        @NotNull
        Idea.Mutable imagine(String name);

        @Override
        @Nullable Idea.Mutable find(String name);
    }

    interface Immutable extends Mind {
        @Override
        @Nullable Idea.Immutable find(String name);
    }

    Generator<Value> makeValueGenerator(@NotNull JSONObject source);
}
