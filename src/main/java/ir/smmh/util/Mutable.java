package ir.smmh.util;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

/**
 * A mutable object is an object that when it is mutated it calls the mutate
 * method which makes it dirty, and when it needs to be clean, it calls the
 * clean method which calls the overridden onClean method if it is dirty.
 * The onClean method must make it non-dirty again.
 */
@ParametersAreNonnullByDefault
public interface Mutable extends AutoCloseable {

    Predicate<Object> MUTABLE = o -> o instanceof Mutable;
    Predicate<Mutable> DIRTY = Mutable::isDirty;

    /**
     * @return Whether or not it is dirty.
     * @implNote You do not need to check for this before calling clean.
     */
    boolean isDirty();

    /**
     * A mere setter with no side-effects
     *
     * @param dirty Whether or not this mutable is dirty
     */
    void setDirty(boolean dirty);

    /**
     * Makes the object dirty. Optionally may do other things as well.
     */
    void preMutate();

    /**
     * Makes the object dirty. Optionally may do other things as well.
     */
    void postMutate();

    boolean onClean();

    default boolean clean() {
        if (isDirty()) {
            if (onClean()) {
                setDirty(false);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    void close() throws CleaningException;

    @FunctionalInterface
    interface Injected extends Mutable {

        @Override
        default void close() throws CleaningException {
            getInjectedMutable().close();
        }

        @Override
        default boolean onClean() {
            return getInjectedMutable().onClean();
        }

        @NotNull Mutable getInjectedMutable();

        @Override
        default boolean isDirty() {
            return getInjectedMutable().isDirty();
        }

        @Override
        default void setDirty(boolean dirty) {
            getInjectedMutable().setDirty(dirty);
        }

        @Override
        default void preMutate() {
            getInjectedMutable().preMutate();
        }

        @Override
        default void postMutate() {
            getInjectedMutable().postMutate();
        }
    }

    interface WithListeners extends Mutable {
        /**
         * Makes the object dirty. Optionally may do other things as well.
         */
        @Override
        default void preMutate() {
            for (FunctionalUtil.OnEventListener listener : getOnPreMutateListeners()) {
                listener.onEvent();
            }
        }

        /**
         * Makes the object dirty. Optionally may do other things as well.
         */
        @Override
        default void postMutate() {
            setDirty(true);
            for (FunctionalUtil.OnEventListener listener : getOnPostMutateListeners()) {
                listener.onEvent();
            }
        }

        @Override
        default void close() throws CleaningException {
            if (isDirty()) {
                for (FunctionalUtil.OnEventListenerWithException<CleaningException> listener : getOnCleanListeners()) {
                    listener.onEventWithException();
                }
                setDirty(false);
            }
        }

        @Override
        default boolean onClean() {
            for (FunctionalUtil.OnEventListenerWithException<CleaningException> listener : getOnCleanListeners()) {
                try {
                    listener.onEventWithException();
                } catch (CleaningException e) {
                    return false;
                }
            }
            return true;
        }

        @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPreMutateListeners();

        @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPostMutateListeners();

        @NotNull Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>> getOnCleanListeners();

        @FunctionalInterface
        interface Injected extends WithListeners {
            @Override
            default void close() throws CleaningException {
                getInjectedMutable().close();
            }

            @Override
            default boolean onClean() {
                return getInjectedMutable().onClean();
            }

            @NotNull Mutable.WithListeners getInjectedMutable();

            @Override
            default boolean isDirty() {
                return getInjectedMutable().isDirty();
            }

            @Override
            default void setDirty(boolean dirty) {
                getInjectedMutable().setDirty(dirty);
            }

            @Override
            default void preMutate() {
                getInjectedMutable().preMutate();
            }

            @Override
            default void postMutate() {
                getInjectedMutable().postMutate();
            }

            @Override
            default @NotNull Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>> getOnCleanListeners() {
                return getInjectedMutable().getOnCleanListeners();
            }

            @Override
            default @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPreMutateListeners() {
                return getInjectedMutable().getOnPreMutateListeners();
            }

            @Override
            default @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPostMutateListeners() {
                return getInjectedMutable().getOnPostMutateListeners();
            }
        }
    }

    class CleaningException extends Exception {
        @SuppressWarnings("PublicConstructor")
        public CleaningException(Throwable cause) {
            super(cause);
        }
    }
}
