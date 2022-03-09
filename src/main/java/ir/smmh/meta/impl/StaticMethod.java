package ir.smmh.meta.impl;

import ir.smmh.meta.Argument;
import ir.smmh.meta.JavaType;
import ir.smmh.meta.TypeSpecifier;
import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.Nullable;

public class StaticMethod extends MethodImpl {
    public StaticMethod(JavaType type, @Nullable String documentation, TypeSpecifier returnType, String name, Sequential<Argument> arguments, String body) {
        super("static " + name, type, documentation, returnType, name, arguments, body);
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
