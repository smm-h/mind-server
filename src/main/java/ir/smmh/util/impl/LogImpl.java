package ir.smmh.util.impl;

import ir.smmh.util.Log;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;

public class LogImpl implements Log {
    private final PrintStream printStream;

    public LogImpl(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public @NotNull PrintStream getPrintStream() {
        return printStream;
    }
}
