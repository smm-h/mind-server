package ir.smmh.lingu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;

public interface Maker<T> {

    @NotNull T makeFromCode(@NotNull Code code) throws MakingException;

    interface LanguageSpecific<T> extends Maker<T>, Language {

        default @NotNull T makeFromTestFile(String filename) throws FileNotFoundException, MakingException {
            return makeFromCode(openTestFile(filename));
        }
    }

    class MakingException extends Exception {
    }
}
