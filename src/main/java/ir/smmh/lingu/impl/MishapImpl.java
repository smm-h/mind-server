package ir.smmh.lingu.impl;

import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.Mishap;
import ir.smmh.lingu.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Something unfortunate that happened to a token(s), individually or
 * collectively, syntactically or semantically, during an process
 * ({@link CodeProcess}), that may or may not be fatal to the process.
 */
public abstract class MishapImpl implements Mishap {
    final boolean fatal;
    private CodeProcess process;

    public MishapImpl(boolean fatal) {
        this.fatal = fatal;
    }

    public static abstract class Caused extends MishapImpl implements Mishap.Caused {

        public final @NotNull Token.Individual token;

        public Caused(Token.Individual token, boolean fatal) {
            super(fatal);
            this.token = token;
        }

        @Override
        public @Nullable Token.Individual getCause() {
            return token;
        }

    }

    @Override
    public boolean isFatal() {
        return fatal;
    }

    @Override
    public CodeProcess getProcess() {
        return process;
    }

    @Override
    public void setProcess(CodeProcess e) {
        if (process == null)
            process = e;
    }

    @Override
    public final String toString() {
        return getReport();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MishapImpl)) return false;

        MishapImpl that = (MishapImpl) o;

        return Objects.equals(this.toString(), that.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
