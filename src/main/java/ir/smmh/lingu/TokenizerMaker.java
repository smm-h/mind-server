package ir.smmh.lingu;

import ir.smmh.lingu.impl.Port;
import ir.smmh.lingu.impl.TokenizerImpl;
import ir.smmh.lingu.impl.TokenizerMakerImpl;
import org.jetbrains.annotations.NotNull;

public interface TokenizerMaker extends Maker.LanguageSpecific<TokenizerImpl> {
    Port<Tokenizer> port = new Port<>("TokenizerMaker:port");

    static @NotNull TokenizerMaker getInstance() {
        return TokenizerMakerImpl.getInstance();
    }
}
