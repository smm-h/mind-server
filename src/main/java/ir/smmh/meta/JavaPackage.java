package ir.smmh.meta;

import ir.smmh.meta.impl.JavaPackageImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public interface JavaPackage {
    static JavaPackage of(String moduleName, String address) throws IOException {
        return new JavaPackageImpl(moduleName, address);
    }

    static JavaPackage of(String address) throws IOException {
        return of("main", address);
    }

    @NotNull Path getPath();

    @NotNull String getModuleName();

    @NotNull String getAddress();

    default JavaPackage subpackage(String name) throws IOException {
        return of(getModuleName(), getAddress() + "." + name);
    }

    default @NotNull String star() {
        return getAddress() + ".*";
    }
}
