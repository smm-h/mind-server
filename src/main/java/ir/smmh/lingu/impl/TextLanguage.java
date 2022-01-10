package ir.smmh.lingu.impl;

import ir.smmh.lingu.Languages;
import ir.smmh.lingu.Maker;

import java.io.FileNotFoundException;

public class TextLanguage extends LanguageImpl {
    public TextLanguage() throws FileNotFoundException, Maker.MakingException {
        super("Text Language", "txt", Languages.getInstance().getTokenizerMaker().makeFromTestFile("text-language"));
    }
}
