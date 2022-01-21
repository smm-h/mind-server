package ir.smmh.storage.impl;

import ir.smmh.storage.Storage;
import ir.smmh.util.FileUtil;
import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
public final class StorageImpl implements Storage {

    private final Log out, err;
    private final String root;

    private StorageImpl(@NotNull String root, @NotNull Log out, @NotNull Log err) {
        super();
        this.root = root;
        this.out = out;
        this.err = err;
    }

    public static Storage of(@NotNull String root, @NotNull Log out, @NotNull Log err) {
        return new StorageImpl(FunctionalUtil.SupplierMayFail.getSafe(() -> FileUtil.touch(root), ""), out, err);
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static Storage of(@NotNull String root, @NotNull String out, @NotNull String err) {
        return of(root, Log.fromFile(out, System.out), Log.fromFile(err, System.err));
    }

    public static Storage of(@NotNull String name) {
        return of("db/" + name, "db/" + name + "/log/OUT.LOG", "db/" + name + "/log/ERR.LOG");
    }

    @Override
    public boolean exists(@NotNull String filename) {
        return Files.exists(Path.of(root, filename));
    }

    @Nullable
    @Override
    public String read(@NotNull String filename) {
        out.log("READING FROM: " + filename);
        try {
            return Files.readString(Path.of(root, filename));
        } catch (IOException e) {
            err.log("FAILED TO READ FROM: " + filename);
            return null;
        }
    }

    @Override
    public boolean write(@NotNull String filename, @NotNull String contents) {
        out.log("WRITING TO: " + filename);
        try {
            Files.writeString(Path.of(root, filename), contents);
            return true;
        } catch (IOException e) {
            err.log("FAILED TO WRITE TO: " + filename);
            return false;
        }
    }

    @Override
    public boolean delete(@NotNull String filename) {
        out.log("DELETING: " + filename);
        try {
            Files.delete(Path.of(root, filename));
            return true;
        } catch (IOException e) {
            out.log("FAILED TO DELETE: " + filename);
            return false;
        }
    }

    @Override
    public void deleteAll() {
        out.log("DELETING ALL FILES");
        try {
            try (Stream<Path> files = Files.list(Path.of(root))) {
                for (Path file : files.collect(Collectors.toList())) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        err.log("FAILED TO DELETE: " + file);
                    }
                }
            }
        } catch (IOException e) {
            err.log("FAILED TO GET A LIST OF ALL FILES");
        }
    }

    @Override
    public String toString() {
        return "Storage@" + root;
    }

}
