package ir.smmh.lingu;

import ir.smmh.lingu.impl.Port;

import java.util.List;

public interface Tokenizer extends Processor {
    Port<List<Token.Individual>> tokenized = new Port<>("Tokenizer:tokenized");

    // public void ignore(String name)

    // public boolean shouldBeIgnored(Token token)

    // private boolean canMake(List<Token> tokens, int index, List<Token> pattern)

    List<Token.Individual> tokenize(Code code);
}