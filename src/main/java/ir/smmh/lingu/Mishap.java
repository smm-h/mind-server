package ir.smmh.lingu;

import ir.smmh.lingu.IndividualTokenType.IndividualToken;

import java.util.Objects;

/**
 * Something unfortunate that happened to a token(s), individually or
 * collectively, syntactically or semantically, during an process
 * ({@link Code.Process}), that may or may not be fatal to the process.
 */
public abstract class Mishap {
    final boolean fatal;
    private Code.Process process;
    public final IndividualToken token;

    public Mishap(IndividualToken token, boolean fatal) {
        this.token = token;
        this.fatal = fatal;
    }

    public void setProcess(Code.Process e) {
        if (process == null)
            process = e;
    }

    public Code.Process getProcess() {
        return process;
    }

    public abstract String getReport();

    // public abstract String getSolution();

    @Override
    public final String toString() {
        return getReport();
    }

    @Override
    public boolean equals(Object other) {
        return Objects.equals(toString(), other.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
