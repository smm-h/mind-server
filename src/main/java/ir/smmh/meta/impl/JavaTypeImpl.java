package ir.smmh.meta.impl;

import ir.smmh.meta.JavaPackage;
import ir.smmh.meta.JavaType;
import ir.smmh.meta.Method;
import ir.smmh.util.FileUtil;
import ir.smmh.util.impl.FormImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

abstract class JavaTypeImpl extends FormImpl implements JavaType {
    private final JavaPackage pkg;
    private final Path path;
    private final String name;

    public JavaTypeImpl(JavaPackage pkg, String name) {
        super(name + ".java");
        this.pkg = pkg;
        this.name = name;
        this.path = FileUtil.pathOf(pkg.getPath() + "/" + name + ".java");
        append(PACKAGE);
        append(IMPORTS);
        enter(PACKAGE, pkg.getAddress());
        append(JAVADOC);
    }

    @Override
    public @NotNull String getTypeName() {
        return name;
    }

    @Override
    public void generateToFile(boolean overwrite) throws IOException {
        generateToFile(path, overwrite);
    }

    @Override
    public @NotNull JavaPackage getPackage() {
        return pkg;
    }

    @Override
    public void addDocumentation(String text) {
        enter(JAVADOC, text);
    }

    @Override
    public void addMethod(Method method) {
        enter(METHODS, method.generate());
    }

    @Override
    public void addImport(String canonicalName) {
        enter(IMPORTS, canonicalName);
    }
}
