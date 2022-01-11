package ir.smmh.lingu.impl;

import ir.smmh.lingu.Language;
import ir.smmh.lingu.Languages;
import ir.smmh.lingu.TokenizerMaker;
import ir.smmh.lingu.groupermaker.GrouperMakerImpl;
import ir.smmh.lingu.json.JSONLanguage;
import ir.smmh.util.Log;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LanguagesImpl implements Languages {

    public static final Languages singleton;

    static {
        try {
            singleton = new LanguagesImpl();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FAILED TO INITIALIZE LANGUAGES");
        }
    }

    private final Log out = Log.fromFile("log/ir/smmh/lingu/OUT.LOG", System.out), err = Log.fromFile("log/ir/smmh/lingu/ERR.LOG", System.err);
    private final HashMap<String, Language> extToLanguage = new HashMap<>();

    private final TokenizerMaker tm;

    private LanguagesImpl() throws Exception {
        new TextLanguage(); // txt
        tm = new TokenizerMakerImpl(); // nlx
        new GrouperMakerImpl(); // ncx
        new TreeLanguage(); // tlg
        new JSONLanguage(); // json
    }

    @Override
    public TokenizerMaker getTokenizerMaker() {
        return tm;
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
