package ir.smmh.mind;

import ir.smmh.storage.Stored;
import ir.smmh.util.Lookup;
import ir.smmh.util.Serializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.function.Supplier;

public interface Mind {

    @Nullable Idea findIdeaByName(String name);

    Supplier<Value> makeValueGenerator(@NotNull JSONObject source);

    /**
     * A mutable mind is an interface that allows you to imagine mutable
     * ideas, mutate them, and once they are coherent, freeze it to get
     * an immutable mind.
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

        @NotNull Lookup<Idea.Mutable> getIdeaLookup();

        default @Nullable Idea.Mutable findIdeaByName(String name) {
            return getIdeaLookup().find(name);
        }
    }

    interface Immutable extends Mind, Serializable {

        @NotNull Lookup<Idea.Immutable> getIdeaLookup();

        default @Nullable Idea.Immutable findIdeaByName(String name) {
            return getIdeaLookup().find(name);
        }
    }
}
