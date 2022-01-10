package ir.smmh.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public interface FileUtil {
    @Contract("_->_")
    static @NotNull String touch(@NotNull String filename) throws IOException, InvalidPathException {
        Files.createDirectories(Path.of(filename));
        return filename;
    }

    static String getExt(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
