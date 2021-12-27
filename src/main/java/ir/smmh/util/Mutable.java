package ir.smmh.util;

import org.jetbrains.annotations.NotNull;

/**
 * A mutable object is an object that when it is mutated it calls the taint
 * method which makes it dirty, and when it needs to be clean, it calls the
 * clean method which calls the overridden onClean method if it is dirty.
 * The onClean method must make it non-dirty again.
 *
 * @implNote Anything mutable might also be an API
 * @see ir.smmh.api.API
 */
public interface Mutable {

    /**
     * @return Whether or not it is dirty.
     * @implNote You do not need to check for this before calling clean.
     */
    boolean isDirty();

    /**
     * Makes the object dirty. Optionally may do other things as well.
     */
    void taint();

    /**
     * Add to this to implement manually cleaning up this object. This is
     * for tasks that though must be done after every mutation, they can be
     * done after several mutations without affecting correctness.
     */
    void addOnCleanListener(OnCleanListener listener);

    /**
     * This is for internal use; this is called within {@link #clean}.
     *
     * @implNote You should not call this directly. Call clean instead.
     */
    void onClean();

    /**
     * Calls {@link #onClean} if it {@link #isDirty}; otherwise does nothing.
     */
    default void clean() {
        if (isDirty()) onClean();
    }

    @FunctionalInterface
    interface OnCleanListener {
        void onClean();
    }

    interface Immutablizable<Immutable> extends Mutable {
        /**
         * Creates and returns a new object of type immutable that is in
         * every way equal to this object, except it cannot be mutated.
         *
         * @return An immutable version of this object
         * @implNote This may cache and return a pre-existing object to
         * increase performance, if it is not dirty (check using isDirty)
         * @implNote Please call clean before implementing this to ensure
         * returning a correct object.
         */
        @NotNull
        Immutable freeze();
    }

    interface Injected extends Mutable {

        @NotNull
        Mutable getInjectedMutable();

        @Override
        default boolean isDirty() {
            return getInjectedMutable().isDirty();
        }

        @Override
        default void addOnCleanListener(OnCleanListener listener) {
            getInjectedMutable().addOnCleanListener(listener);
        }

        @Override
        default void taint() {
            getInjectedMutable().taint();
        }

        @Override
        default void onClean() {
            getInjectedMutable().onClean();
        }
    }
}
