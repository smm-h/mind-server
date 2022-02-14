package ir.smmh.lingu.impl;

import ir.smmh.lingu.Maker;
import ir.smmh.lingu.TokenizerMaker;

import java.io.FileNotFoundException;

public class TextLanguage extends LanguageImpl {
    public TextLanguage() throws FileNotFoundException, Maker.MakingException {
        super("Text Language", "txt", TokenizerMaker.getInstance().makeFromTestFile("text-language"));
    }
}
