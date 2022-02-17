package ir.smmh.lingu;

import ir.smmh.util.jile.OpenFile;
import org.jetbrains.annotations.NotNull;

public interface Code {
    @NotNull OpenFile getOpenFile();

    @NotNull Language getLanguage();
}
