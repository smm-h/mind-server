package ir.smmh.lingu.impl;

import ir.smmh.lingu.*;
import ir.smmh.util.jile.OpenFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LanguageImpl implements Language {

    private final String name, langPath;
    private final @Nullable String primaryExt;

    private final Processor processor;
    private Maker<?> mainMaker;

    public LanguageImpl(String name, String langPath, @Nullable String primaryExt, Processor processor) {
        this.name = name;
        this.langPath = langPath;
        this.primaryExt = primaryExt;
        this.processor = processor;

        // if (processor instanceof Tokenizer)
        // setMainTokenizer((Tokenizer) processor);

        if (primaryExt != null)
            Languages.getInstance().associateExtWithLanguage(primaryExt, this);
    }

    public LanguageImpl(String name, String primaryExt, Processor processor) {
        this(name, primaryExt, primaryExt, processor);
    }

    @Override
    public @NotNull Code openTestFile(@NotNull String filename) {
        return new CodeImpl(OpenFile.of(langPath + "/" + filename + "." + primaryExt), this);
    }

    public Maker<?> getMainMaker() {
        return mainMaker;
    }

    public void setMainMaker(Maker<?> maker) {
        this.mainMaker = maker;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getLangPath() {
        return langPath;
    }

    @Override
    public @Nullable String getPrimaryExt() {
        return primaryExt;
    }

    @Override
    public @NotNull Processor getProcessor() {
        return processor;
    }
}