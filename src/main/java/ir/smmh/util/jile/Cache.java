package ir.smmh.util.jile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A cache for objects that are immutable (wrapper) and created
 * deterministically from another immutable type (contents).
 */
@FunctionalInterface
public interface Cache<Wrapper, Contents> {

    @NotNull Wrapper get(Contents contents);

    interface Extended<Wrapper, Contents> extends Cache<Wrapper, Contents> {

        @Nullable Wrapper find(Contents contents);

        /**
         * It is very important that this process is deterministic
         */
        void create(Contents contents);

        boolean exists(Contents contents);

        @NotNull
        default Wrapper get(Contents contents) {
            if (!exists(contents)) {
                create(contents);
            }
            return Objects.requireNonNull(find(contents));
        }
    }
}
