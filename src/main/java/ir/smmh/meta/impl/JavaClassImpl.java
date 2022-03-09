package ir.smmh.meta.impl;

import ir.smmh.meta.JavaClass;
import ir.smmh.meta.JavaPackage;

public class JavaClassImpl extends JavaTypeImpl implements JavaClass {
    public JavaClassImpl(JavaPackage pkg, String name) {
        super(pkg, name);
        append("public class ");
        append(NAME);
        append(" ");
        append(SUPER_CLASS);
        append(SUPER_INTERFACES);
        append(METHODS);
        enter(NAME, name);
    }
}
