package ir.smmh.lingu;

import ir.smmh.jile.common.Singleton;

import java.util.HashMap;

public class Languages implements Singleton {

    private Languages() {
    }

    private static Languages singleton;

    public static Languages singleton() {
        if (singleton == null) {
            singleton = new Languages();
        }
        return singleton;
    }

    private final HashMap<String, Language> extToLanguage = new HashMap<>();

    public void associateExtWithLanguage(String ext, Language language) {
        // if (extToLanguage == null) {
        // extToLanguage = new HashMap<String, Language>();
        // }
        ext = ext.toLowerCase();
        extToLanguage.put(ext, language);
        // System.out.println("*." + ext + " <- " + language.name);
    }

    public Language getLanguageByExt(String ext) {
        Language language = null;
        ext = ext.toLowerCase();
        if (extToLanguage.containsKey(ext)) {
            language = extToLanguage.get(ext);
        }
        return language;
    }
}
