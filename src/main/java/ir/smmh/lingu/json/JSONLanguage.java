package ir.smmh.lingu.json;


import ir.smmh.lingu.Maker;
import ir.smmh.lingu.impl.LanguageImpl;
import ir.smmh.lingu.processors.impl.MultiprocessorImpl;
import ir.smmh.tree.jile.Tree;

public class JSONLanguage extends LanguageImpl {

    private static JSONLanguage singleton;
    public final Maker<Tree<JSON.Element>> maker = code -> {
        // TODO Auto-generated method stub
        return null;
    };

    public JSONLanguage() {
        super("JSON", "json", new MultiprocessorImpl());
    }

    public static JSONLanguage singleton() {
        if (singleton == null) {
            singleton = new JSONLanguage();
        }
        return singleton;
    }
}
