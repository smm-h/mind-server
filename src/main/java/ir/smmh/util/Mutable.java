package ir.smmh.util;

import org.jetbrains.annotations.NotNull;

/**
 * A mutable object is an object that when it is mutated it calls the taint
 * method which makes it dirty, and when it needs to be clean, it calls the
 * clean method which calls the overridden onClean method if it is dirty.
 * The onClean method must make it non-dirty again.
 */
public interface Mutable<Immutable> {

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
     * Override this to implement manually cleaning this object. This is
     * for computationally intense tasks that must not be done at every mutation.
     *
     * @implNote You should not call this directly. Call clean instead.
     */
    void onClean();

    /**
     * Calls onClean if it is dirty; otherwise does nothing.
     */
    default void clean() {
        if (isDirty()) onClean();
    }
}
