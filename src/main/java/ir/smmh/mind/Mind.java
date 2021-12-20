package ir.smmh.mind;

import ir.smmh.mind.impl.NumberValue;
import ir.smmh.mind.impl.StringValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface Mind {
    /**
     * Finds and returns an idea with a given name.
     *
     * @param name The name of the idea
     * @return The idea in this mind with that name
     */
    @Nullable
    Idea find(String name);

    default Value valueOf(JSONObject object) {
        final String kind = object.getString("~");
        switch (kind) {
            case "number":
                return new NumberValue(object.getNumber("value"));
            case "string":
                return new StringValue(object.getString("value"));
            default:
                return find(kind).deserialize(object);
        }
    }

    /**
     * A mind is an interface that allows you to imagine coherent ideas,
     * mutate them, and
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
}
