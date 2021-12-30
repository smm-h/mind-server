package ir.smmh.lingu;

import ir.smmh.lingu.IndividualTokenType.IndividualToken;
import ir.smmh.lingu.processors.Processor;

import java.util.List;

public interface Tokenizer extends Processor {
    List<IndividualToken> tokenize(Code code);

    // public void ignore(String name)

    // public boolean shouldBeIgnored(Token token)

    // private boolean canMake(List<Token> tokens, int index, List<Token> pattern)

    Port<List<IndividualToken>> tokenized = new Port<List<IndividualToken>>("Tokenizer:tokenized");
}