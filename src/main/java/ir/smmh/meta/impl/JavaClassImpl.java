package ir.smmh.meta.impl;

import ir.smmh.meta.JavaClass;
import ir.smmh.meta.JavaInterface;
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

    public JavaClassImpl(JavaPackage pkg, JavaInterface impls) {
        this(pkg, impls.getTypeName() + "Impl");
        enter(JavaClass.SUPER_INTERFACES, impls.getTypeName());
        addImport(impls);
    }

    public JavaClassImpl(JavaPackage pkg, JavaClass superclass, JavaInterface impls) {
        this(pkg, impls.getTypeName() + "Impl");
        enter(JavaClass.SUPER_CLASS, superclass.getTypeName());
        addImport(superclass);
        enter(JavaClass.SUPER_INTERFACES, impls.getTypeName());
        addImport(impls);
    }

    public JavaClassImpl(JavaPackage pkg, Class<?> impls) {
        this(pkg, impls.getTypeName() + "Impl");
        assert impls.isInterface();
        enter(JavaClass.SUPER_INTERFACES, impls.getTypeName());
        addImport(impls);
    }

    public JavaClassImpl(JavaPackage pkg, Class<?> superclass, Class<?> impls) {
        this(pkg, impls.getTypeName() + "Impl");
        assert !superclass.isInterface();
        enter(JavaClass.SUPER_CLASS, superclass.getTypeName());
        addImport(superclass);
        assert impls.isInterface();
        enter(JavaClass.SUPER_INTERFACES, impls.getTypeName());
        addImport(impls);
    }

    @Override
    public String toString() {
        return "[Class] " + getTypeName();
    }
}
