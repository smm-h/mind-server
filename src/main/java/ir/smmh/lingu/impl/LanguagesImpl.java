package ir.smmh.lingu.impl;

import ir.smmh.lingu.Language;
import ir.smmh.lingu.Languages;
import ir.smmh.util.Log;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LanguagesImpl implements Languages {

    private static Languages instance;
    private final Log out = Log.fromFile("log/ir/smmh/lingu/OUT.LOG", System.out), err = Log.fromFile("log/ir/smmh/lingu/ERR.LOG", System.err);
    private final HashMap<String, Language> extToLanguage = new HashMap<>();
//    private final TokenizerMaker tm;

    private LanguagesImpl() {
//        new TextLanguage(); // txt
//        tm = new TokenizerMakerImpl(); // nlx
//        new GrouperMakerImpl(); // ncx
//        new TreeLanguage(); // tlg
//        new JSONLanguage(); // json
    }

    public static Languages getInstance() {
        return instance == null ? (instance = new LanguagesImpl()) : instance;
    }

    @Override
    public @NotNull Log getOut() {
        return out;
    }

    @Override
    public @NotNull Log getErr() {
        return err;
    }

    @Override
    public void associateExtWithLanguage(@NotNull String ext, @NotNull Language language) {
        ext = ext.toLowerCase();
        extToLanguage.put(ext, language);
        // System.out.println("*." + ext + " <- " + language.name);
    }

    @Override
    public Language getLanguageByExt(@NotNull String ext) {
        Language language = null;
        ext = ext.toLowerCase();
        if (extToLanguage.containsKey(ext)) {
            language = extToLanguage.get(ext);
        }
        return language;
    }
}
