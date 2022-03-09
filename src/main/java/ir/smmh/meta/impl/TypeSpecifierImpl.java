package ir.smmh.meta.impl;

import ir.smmh.meta.TypeSpecifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TypeSpecifierImpl implements TypeSpecifier {
    private final String type;
    private final @Nullable Boolean isNullable;
    private final boolean isPrimitive;

    public TypeSpecifierImpl(@Nullable Boolean isNullable, String type) {
        this(isNullable, type, false);
    }

    public TypeSpecifierImpl(@Nullable Boolean isNullable, String type, boolean isPrimitive) {
        if (isPrimitive && isNullable != null && isNullable)
            throw new IllegalArgumentException("type-specifier cannot be both nullable and primitive");
        this.isNullable = isNullable;
        this.type = type;
        this.isPrimitive = isPrimitive;
    }

    @Override
    public @Nullable Boolean isNullable() {
        return isNullable;
    }

    @Override
    public @NotNull String getTypeName() {
        return type;
    }

    @Override
    public boolean isPrimitive() {
        return isPrimitive;
    }

    @Override
    public @NotNull String generate() {
        return (isPrimitive || isNullable == null ? "" : "@" + (isNullable ? "Nullable" : "NotNull") + " ") + type;
    }
}
