package ir.smmh.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        Files.createDirectories(pathOf(filename).getParent());
        return filename;
    }

    @SuppressWarnings("MagicCharacter")
    static String getExt(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.getDefault());
    }

    static Path pathOf(String s) {
        return new File(s).toPath();
    }

    static Path pathOf(String root, String s) {
        return pathOf(root + "/" + s);
    }

    static String Files_readString(Path p) throws IOException {
        return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
    }

    static void Files_writeString(Path p, String s) throws IOException {
        Files.write(p, s.getBytes(StandardCharsets.UTF_8));
    }
}
