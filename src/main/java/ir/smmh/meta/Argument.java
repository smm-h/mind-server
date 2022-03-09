package ir.smmh.meta;

import org.jetbrains.annotations.NotNull;

public interface Argument extends CanGenerate {
    @NotNull TypeSpecifier getType();

    @NotNull String getName();
}
