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
    default void postMutate() {
        setDirty(true);
        for (OnTaintListener listener : getOnTaintListeners()) {
            listener.onTaint();
        }
    }

    @NotNull Listeners<OnTaintListener> getOnTaintListeners();

    @NotNull Listeners<OnCleanListener> getOnCleanListeners();

    default boolean clean() {
        if (isDirty()) {
            for (OnCleanListener listener : getOnCleanListeners()) {
                try {
                    listener.onClean();
                } catch (CleaningException e) {
                    return false;
                }
            }
            setDirty(false);
        }
        return true;
    }

    @FunctionalInterface
    interface OnCleanListener {
        void onClean() throws CleaningException;
    }

    @FunctionalInterface
    interface OnTaintListener {
        void onTaint();
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
        default @NotNull Listeners<OnCleanListener> getOnCleanListeners() {
            return getInjectedMutable().getOnCleanListeners();
        }

        @Override
        default @NotNull Listeners<OnTaintListener> getOnTaintListeners() {
            return getInjectedMutable().getOnTaintListeners();
        }

        @Override
        default void postMutate() {
            getInjectedMutable().postMutate();
        }
    }

    class CleaningException extends Exception {
        public CleaningException(Throwable throwable) {
            super(throwable);
        }
    }
}
