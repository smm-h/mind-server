package ir.smmh.lingu.json;

import ir.smmh.jile.common.Singleton;
import ir.smmh.lingu.Language;
import ir.smmh.lingu.impl.CodeImpl;
import ir.smmh.lingu.processors.Multiprocessor;
import ir.smmh.tree.jile.Tree;

public class JSONLanguage extends Language implements Singleton {

    private static JSONLanguage singleton;

    public static JSONLanguage singleton() {
        if (singleton == null) {
            singleton = new JSONLanguage();
        }
        return singleton;
    }

    public JSONLanguage() {
        super("JSON", "json", new Multiprocessor());
    }

    public final Maker<Tree<JSON.Element>> maker = new Maker<Tree<JSON.Element>>() {
        @Override
        public Tree<JSON.Element> make(CodeImpl code) {
            // TODO Auto-generated method stub
            return null;
        }
    };
}
