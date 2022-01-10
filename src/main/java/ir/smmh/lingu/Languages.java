package ir.smmh.lingu;

import ir.smmh.lingu.impl.LanguagesImpl;
import ir.smmh.lingu.impl.TokenizerMakerImpl;
import ir.smmh.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Languages {
    static @NotNull Languages getInstance() {
        return LanguagesImpl.singleton;
    }

    @NotNull Log getOut();

    @NotNull Log getErr();

    void associateExtWithLanguage(@NotNull String ext, @NotNull Language language);

    @Nullable Language getLanguageByExt(@NotNull String ext);

    TokenizerMaker getTokenizerMaker();
}
