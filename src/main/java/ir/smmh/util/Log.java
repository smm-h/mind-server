package ir.smmh.util;

import ir.smmh.util.impl.LogImpl;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.InvalidPathException;
import java.time.Instant;

public interface Log {
    static Log fromFile(@NotNull String filename, @NotNull PrintStream defaultStream) {
        try {
            return new LogImpl(new PrintStream(new FileOutputStream(FileUtil.touch(filename), true)));
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (InvalidPathException e) {
            System.err.println("Invalid filename: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fromStream(defaultStream);
    }

    // TODO replace all System.out.print with out.log
    // (and all System.err.print with err.log)

    static Log fromStream(@NotNull PrintStream stream) {
        return new LogImpl(stream);
    }

    @NotNull PrintStream getPrintStream();

    default void log() {
        getPrintStream().println();
    }

    default void log(@NotNull final String text) {
        final PrintStream log = getPrintStream();
        log.print(Instant.now());
        log.print(" \t ");
        log.println(text);
    }

    default void log(@NotNull final Throwable error) {
        log(error.getMessage());
    }
}
