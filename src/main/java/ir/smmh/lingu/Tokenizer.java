package ir.smmh.lingu;

import ir.smmh.lingu.impl.CodeImpl;
import ir.smmh.lingu.impl.Port;
import ir.smmh.lingu.processors.Processor;

import java.util.List;

public interface Tokenizer extends Processor {
    List<Token.Individual> tokenize(CodeImpl code);

    // public void ignore(String name)

    // public boolean shouldBeIgnored(Token token)

    // private boolean canMake(List<Token> tokens, int index, List<Token> pattern)

    Port<List<Token.Individual>> tokenized = new Port<>("Tokenizer:tokenized");
}