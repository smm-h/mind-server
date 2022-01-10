package ir.smmh.lingu.impl;

import ir.smmh.lingu.Maker;

import java.io.FileNotFoundException;

public class TextLanguage extends LanguageImpl {

    private static TextLanguage singleton;

    public TextLanguage() throws FileNotFoundException, Maker.MakingException {
        super("Text Language", "txt", TokenizerMaker.singleton().makeFromTestFile("text-language"));
    }

    public static TextLanguage singleton() {
        if (singleton == null) {
            try {
                singleton = new TextLanguage();
            } catch (FileNotFoundException e) {
                throw new RuntimeException("could not load text language");
            } catch (Maker.MakingException e) {
                throw new RuntimeException("could not make text language");
            }
        }
        return singleton;
    }
}
