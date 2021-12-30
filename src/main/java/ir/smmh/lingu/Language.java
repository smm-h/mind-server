package ir.smmh.lingu;

import ir.smmh.jile.common.Resource;
import ir.smmh.lingu.processors.Processor;

import java.util.Objects;

public abstract class Language {

    public final String name, langPath, primaryExt;

    public final Processor processor;

    public Language(String name, String langPath, String primaryExt, Processor processor) {
        this.name = name;
        this.langPath = langPath;
        this.primaryExt = primaryExt;
        this.processor = processor;

        // if (processor instanceof Tokenizer)
        // setMainTokenizer((Tokenizer) processor);

        if (primaryExt != null)
            Languages.singleton().associateExtWithLanguage(primaryExt, this);
    }

    public Language(String name, String primaryExt, Processor processor) {
        this(name, primaryExt, primaryExt, processor);
    }

    public Resource find(String address) {
        return Objects.requireNonNull(Resource.of(langPath + "/" + address + "." + primaryExt));
    }

    private Maker<?> mainMaker;

    public void setMainMaker(Maker<?> maker) {
        this.mainMaker = maker;
    }

    public Maker<?> getMainMaker() {
        return mainMaker;
    }

    public abstract class Maker<T> implements Linter {

        public abstract T make(Code code);

        public T makeFrom(String address) {
            return make(new Code(find(address), Language.this));
        }

    }
}