package ir.smmh.meta.impl;

import ir.smmh.meta.Argument;
import ir.smmh.meta.JavaInterface;
import ir.smmh.meta.TypeSpecifier;
import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.Nullable;

public class OverrideableMethod extends MethodImpl {
    public OverrideableMethod(JavaInterface type, @Nullable String documentation, TypeSpecifier returnType, String name, Sequential<Argument> arguments) {
        super("overrideable " + name, type, documentation, returnType, name, arguments, null);
    }
}
