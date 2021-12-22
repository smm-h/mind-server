package ir.smmh.storage;

import ir.smmh.util.Mutable;
import ir.smmh.util.Serializable;
import org.jetbrains.annotations.NotNull;

public interface PermanentlyStored extends Serializable, Mutable<String> {
    @NotNull
    String getIdentifier();

    @NotNull
    PermanentStorage getStorage();

    @Override
    default @NotNull String freeze() {
        return serialize();
    }

    @Override
    default void onClean() {
        getStorage().write(getIdentifier(), serialize());
    }
}
