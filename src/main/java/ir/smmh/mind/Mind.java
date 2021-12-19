package ir.smmh.mind;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public interface Mind {
    /**
     * A mind is an interface that allows you to imagine coherent ideas,
     * mutate them, and
     */
    interface Mutable extends Mind, ir.smmh.common.Mutable<Immutable> {
        /**
         * Find and return an idea with a given name.
         * @param name The name of an idea
         * @param create Create the idea if none is found with that name
         * @return The mutable idea in this mind with that name
         */
        @Nullable
        Idea.Mutable imagine(String name, boolean create);
    }

    interface Immutable {
        @NotNull
        LocalDateTime getCreatedOn();
    }
}
