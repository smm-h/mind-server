package ir.smmh.lingu;

import ir.smmh.lingu.impl.Port;
import ir.smmh.lingu.impl.TokenizerImpl;

public interface TokenizerMaker extends Maker.LanguageSpecific<TokenizerImpl> {
    Port<Tokenizer> port = new Port<>("TokenizerMaker:port");
}
