package ir.smmh.meta.impl;

import ir.smmh.meta.Argument;
import ir.smmh.meta.TypeSpecifier;
import org.jetbrains.annotations.NotNull;

public class ArgumentImpl implements Argument {
    private final @NotNull String name;
    private final TypeSpecifier type;

    public ArgumentImpl(TypeSpecifier type, @NotNull String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public @NotNull TypeSpecifier getType() {
        return type;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String generate() {
        return type.generate() + " " + name;
    }

    @Override
    public String toString() {
        return "[Argument] " + generate();
    }
}
