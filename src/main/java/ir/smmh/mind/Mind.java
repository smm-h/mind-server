package ir.smmh.mind;

import ir.smmh.mind.impl.MutableIdeaImpl;
import ir.smmh.storage.Stored;
import ir.smmh.util.Map;
import ir.smmh.util.Named;
import ir.smmh.util.Serializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Mind extends Named {

    Iterable<String> overIdeaNames();

    Iterable<Idea> overIdeas();

    @Nullable Idea findIdeaByName(String name);

    Supplier<Value> makeValueGenerator(@NotNull JSONObject source);

    /**
     * A mutable mind is an interface that allows you to imagine mutable
     * ideas and mutate them.
     */
    interface Mutable extends Mind, Stored {
        /**
         * Finds and returns an idea with a given name. It creates the
         * idea if none with that name exists.
         *
         * @param name The name of an idea
         * @return A mutable idea in this mind with that name
         */
        @NotNull Idea.Mutable imagine(String name);

        @NotNull Function<String, MutableIdeaImpl> getIdeaLookup();

        default @Nullable Idea.Mutable findIdeaByName(String name) {
            return getIdeaLookup().apply(name);
        }
    }

    interface Immutable extends Mind, Serializable {

        @NotNull Map.SingleValue<String, Idea.Immutable> getIdeaLookup();

        default @Nullable Idea.Immutable findIdeaByName(String name) {
            return getIdeaLookup().get(name);
        }
    }
}
