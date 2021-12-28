package ir.smmh.util;

import org.jetbrains.annotations.NotNull;

public interface Named {
    static @NotNull String nameOf(Object object) {
        if (object == null) {
            return "NULL";
        } else if (object instanceof Named) {
            return ((Named) object).getName();
        } else {
            return object.toString();
        }
    }

    @NotNull String getName();
}
