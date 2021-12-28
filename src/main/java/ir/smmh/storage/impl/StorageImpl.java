package ir.smmh.storage.impl;

import ir.smmh.storage.Storage;
import ir.smmh.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StorageImpl implements Storage {

    private final Log out, err;
    private final String root;

    public StorageImpl(@NotNull String root, @NotNull Log out, @NotNull Log err) {
        this.root = root;
        this.out = out;
        this.err = err;
    }

    public StorageImpl(@NotNull String root, @NotNull String out, @NotNull String err) {
        this(root, Log.fromOutStream(out), Log.fromErrStream(err));
    }

    public StorageImpl(@NotNull String name) {
        this("db/" + name, "log/" + name + "_OUT.LOG", "log/" + name + "_ERR.LOG");
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
        final Stream<Path> files;
        try {
            files = Files.list(Path.of(root));
        } catch (IOException e) {
            err.log("FAILED TO GET A LIST OF ALL FILES");
            return;
        }
        for (Path file : files.collect(Collectors.toList())) {
            try {
                Files.delete(file);
            } catch (IOException e) {
                err.log("FAILED TO DELETE: " + file);
            }
        }
    }
}
