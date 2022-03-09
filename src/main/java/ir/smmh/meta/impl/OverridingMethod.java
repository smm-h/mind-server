package ir.smmh.meta.impl;

import ir.smmh.meta.JavaClass;
import ir.smmh.meta.Method;

public class OverridingMethod extends MethodImpl {
    public OverridingMethod(JavaClass type, OverrideableMethod overrideable, String body) {
        super("overriding " + overrideable.getName(), type,
                overrideable.getDocumentation(),
                overrideable.getReturnType(),
                overrideable.getName(),
                overrideable.getArguments(),
                body);
        enter(Method.ANNOTATIONS, "Override");
    }
}
