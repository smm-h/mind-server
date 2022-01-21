package ir.smmh.mind;

import ir.smmh.mind.impl.MutableIdeaImpl;
import ir.smmh.nile.verbs.CanSerialize;
import ir.smmh.storage.Stored;
import ir.smmh.util.Named;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("ClassNamePrefixedWithPackageName")
public interface Mind extends Named {

    @NotNull Iterable<String> overIdeaNames();

    @NotNull Iterable<? extends Idea> overIdeas();

    @Nullable Idea findIdeaByName(String ideaName);

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
         * @param ideaName The name of an idea
         * @return A mutable idea in this mind with that name
         */
        @NotNull Idea.Mutable imagine(String ideaName);

        @NotNull Function<String, MutableIdeaImpl> getIdeaLookup();

        @Override
        default @Nullable Idea.Mutable findIdeaByName(String ideaName) {
            return getIdeaLookup().apply(ideaName);
        }
    }

    interface Immutable extends Mind, CanSerialize {

        @NotNull Function<String, Idea.Immutable> getIdeaLookup();

        @Override
        default @Nullable Idea.Immutable findIdeaByName(String ideaName) {
            return getIdeaLookup().apply(ideaName);
        }
    }
}
