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
public interface Mutable {

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
    default void mutate() {
        for (FunctionalUtil.OnEventListener listener : getOnPreMutateListeners()) {
            listener.onEvent();
        }
        setDirty(true);
        for (FunctionalUtil.OnEventListener listener : getOnPostMutateListeners()) {
            listener.onEvent();
        }
    }

    @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPreMutateListeners();

    @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPostMutateListeners();

    @NotNull Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>> getOnCleanListeners();

    default boolean clean() {
        if (isDirty()) {
            for (FunctionalUtil.OnEventListenerWithException<CleaningException> listener : getOnCleanListeners()) {
                try {
                    listener.onEventWithException();
                } catch (CleaningException e) {
                    return false;
                }
            }
            setDirty(false);
        }
        return true;
    }

    interface Injected extends Mutable {

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

        @Override
        default void mutate() {
            getInjectedMutable().mutate();
        }
    }

    class CleaningException extends Exception {
        public CleaningException(Throwable throwable) {
            super(throwable);
        }
    }
}
