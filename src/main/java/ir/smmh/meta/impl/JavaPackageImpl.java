package ir.smmh.meta.impl;

import ir.smmh.meta.JavaPackage;
import ir.smmh.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaPackageImpl implements JavaPackage {
    private final @NotNull Path path;
    private final @NotNull String moduleName, address;

    public JavaPackageImpl(@NotNull String moduleName, @NotNull String address) throws IOException {
        this.path = Path.of("src/" + moduleName + "/java/" + StringUtil.replaceCharacter(address, '.', '/'));
        this.moduleName = moduleName;
        this.address = address;
        Files.createDirectories(path);
    }

    @Override
    public @NotNull Path getPath() {
        return path;
    }

    @Override
    public @NotNull String getModuleName() {
        return moduleName;
    }

    @Override
    public @NotNull String getAddress() {
        return address;
    }
}
