package ir.smmh.util;

import ir.smmh.util.impl.LogImpl;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.Instant;

public interface Log {
    @NotNull PrintStream getPrintStream();

    // TODO replace all System.out.print with out.log
    // (and all System.err.print with err.log)

    static Log fromOutStream(@NotNull final String filename) {
        try {
            return new LogImpl(new PrintStream(new FileOutputStream(filename, true)));
        } catch (FileNotFoundException e) {
            System.err.println("Failed to open " + filename + ", using System.out instead");
            return new LogImpl(System.out);
        }
    }

    static Log fromErrStream(@NotNull final String filename) {
        try {
            return new LogImpl(new PrintStream(new FileOutputStream(filename, true)));
        } catch (FileNotFoundException e) {
            System.err.println("Failed to open " + filename + ", using System.err instead");
            return new LogImpl(System.err);
        }
    }

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
