package ir.smmh.meta.impl;

import ir.smmh.meta.Argument;
import ir.smmh.meta.JavaClass;
import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.Nullable;

public class Constructor extends MethodImpl {
    public Constructor(JavaClass type, @Nullable String documentation, Sequential<Argument> arguments, @Nullable String body) {
        super("constructor of " + type.getTypeName(), type, documentation, type.getSpecifier(false), type.getTypeName(), arguments, body);
    }
}
