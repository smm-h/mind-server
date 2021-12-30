package ir.smmh.lingu;

import java.util.Objects;

/**
 * Something unfortunate that happened to a token(s), individually or
 * collectively, syntactically or semantically, during an process
 * ({@link CodeProcess}), that may or may not be fatal to the process.
 */
public abstract class AbstractMishap implements Mishap {
    final boolean fatal;
    private CodeProcess process;
    public final Token.Individual token;

    @Override
    public Token.Individual getToken() {
        return token;
    }

    @Override
    public boolean isFatal() {
        return fatal;
    }

    public AbstractMishap(Token.Individual token, boolean fatal) {
        this.token = token;
        this.fatal = fatal;
    }

    @Override
    public void setProcess(CodeProcess e) {
        if (process == null)
            process = e;
    }

    @Override
    public CodeProcess getProcess() {
        return process;
    }

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
