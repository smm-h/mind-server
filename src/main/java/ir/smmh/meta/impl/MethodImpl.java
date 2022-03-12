package ir.smmh.meta.impl;

import ir.smmh.meta.*;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.impl.FormImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class MethodImpl extends FormImpl implements Method {
    private final JavaType type;
    private final String name;
    private final TypeSpecifier returnType;
    private final Sequential<Argument> arguments;
    private final @Nullable String documentation, body;

    MethodImpl(String title, JavaType type, @Nullable String documentation, TypeSpecifier returnType, String name, Sequential<Argument> arguments, @Nullable String body) {
        super(title);
        this.type = type;
        this.documentation = documentation;
        this.returnType = returnType;
        this.name = name;
        this.arguments = arguments;
        this.body = body;

        append(JavaType.JAVADOC);
        append(ANNOTATIONS);
        append(QUALIFIERS);
        append(RETURN_TYPE);
        append(NAME);
        append(ARGUMENTS);
        append(BODY);

        enter(NAME, getName());
        enter(BODY, getBody());
        enter(JavaType.JAVADOC, getDocumentation());
        if (getType() instanceof JavaClass)
            enter(QUALIFIERS, "public");
        if (isStatic())
            enter(QUALIFIERS, "static");
        if (!(this instanceof Constructor))
            enter(RETURN_TYPE, getReturnType().generate());
        for (Argument argument : getArguments())
            enter(ARGUMENTS, argument.generate());
    }

    @Override
    public void addToType() {
        this.type.addMethod(this);
    }

    @Override
    public @NotNull JavaType getType() {
        return type;
    }

    @Override
    public @Nullable String getDocumentation() {
        return documentation;
    }

    @Override
    public @NotNull TypeSpecifier getReturnType() {
        return returnType;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Sequential<Argument> getArguments() {
        return arguments;
    }

    @Override
    public @Nullable String getBody() {
        return body;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public String toString() {
        return "[Method] " + name + " of " + type.getTypeName();
    }
}
