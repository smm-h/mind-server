package ir.smmh.util;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.Instant;

public interface Logger {
    @NotNull PrintStream getLog();

    static PrintStream getLog(@NotNull final String name) {
        final String filename = "log/" + name + ".log";
        try {
            return new PrintStream(new FileOutputStream(filename, true));
        } catch (FileNotFoundException e) {
            System.err.println("Failed to open " + filename + ", using System.err instead");
            return System.err;
        }
    }

    default void log() {
        getLog().println();
    }

    default void log(@NotNull final String text) {
        final PrintStream log = getLog();
        log.print(Instant.now());
        log.print(" \t ");
        log.println(text);
    }

    default void log(@NotNull final Throwable error) {
        log(error.getMessage());
    }
}
