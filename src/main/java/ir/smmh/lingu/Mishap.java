package ir.smmh.lingu;

public interface Mishap {
    // TODO remove this method
    void setProcess(CodeProcess e);

    CodeProcess getProcess();

    String getReport();

    // String getSolution();
    boolean isFatal();

    Token.Individual getToken();
}
