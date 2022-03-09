package ir.smmh.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TypeSpecifier extends CanGenerate {

    @Nullable Boolean isNullable();

    @NotNull String getTypeName();

    boolean isPrimitive();
}
