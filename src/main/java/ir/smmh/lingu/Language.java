package ir.smmh.lingu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;

public interface Language {
    @NotNull String getName();

    @NotNull String getLangPath();

    @Nullable String getPrimaryExt();

    @NotNull Processor getProcessor();

    @NotNull Code openTestFile(@NotNull String filename) throws FileNotFoundException;
}
