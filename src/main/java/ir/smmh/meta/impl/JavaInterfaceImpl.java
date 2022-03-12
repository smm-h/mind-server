package ir.smmh.meta.impl;

import ir.smmh.meta.JavaInterface;
import ir.smmh.meta.JavaPackage;

public class JavaInterfaceImpl extends JavaTypeImpl implements JavaInterface {
    public JavaInterfaceImpl(JavaPackage pkg, String name) {
        super(pkg, name);
        append("public interface ");
        append(NAME);
        append(" ");
        append(SUPER_INTERFACES);
        append(METHODS);
        enter(NAME, name);
    }

    @Override
    public String toString() {
        return "[Interface] " + getTypeName();
    }
}
