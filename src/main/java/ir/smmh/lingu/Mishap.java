package ir.smmh.lingu;

import java.util.function.Predicate;

public interface Mishap {
    Predicate<Mishap> FATAL = Mishap::isFatal;

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
