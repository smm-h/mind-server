package ir.smmh.lingu;

import ir.smmh.util.jile.Quality;

public interface Mishap {
    Quality<Mishap> FATAL = Mishap::isFatal;

    CodeProcess getProcess();

    // TODO remove this method
    void setProcess(CodeProcess e);

    // String TODO getSolution();

    String getReport();

    boolean isFatal();

    interface Caused extends Mishap {
        Token.Individual getCause();
    }
}
