package ir.smmh.storage;

import ir.smmh.util.Mutable;
import ir.smmh.util.Named;
import ir.smmh.util.Serializable;
import org.jetbrains.annotations.NotNull;

/**
 * A permanently stored object is anything that is meant to be mutable
 * within memory but must also be backed-up onto a file, so that no
 * change on it is ever lost. So do not forget to call mutate upon
 * every change, which is usually just the setters.
 */
public interface Stored extends Serializable, Mutable.WithListeners, Named {

    default void setupStored() {
        getOnCleanListeners().add(() -> getStorage().write(getName(), serialize()));
    }

    @NotNull Storage getStorage();
}
