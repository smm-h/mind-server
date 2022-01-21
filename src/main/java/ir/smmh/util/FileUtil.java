package ir.smmh.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Locale;

@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public interface FileUtil {
    /**
     * Creates all the missing directories in a filename.
     *
     * @param filename the name of a file whose parent directories are to be created
     * @return the same as the entered filename
     * @throws IOException          if the directories cannot be created
     * @throws InvalidPathException if the filename provided is not a valid path
     */
    @Contract("_->_")
    static @NotNull String touch(@NotNull String filename) throws IOException {
        Files.createDirectories(Path.of(filename).getParent());
        return filename;
    }

    @SuppressWarnings("MagicCharacter")
    static String getExt(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.getDefault());
    }
}
