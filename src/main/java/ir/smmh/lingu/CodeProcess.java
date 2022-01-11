package ir.smmh.lingu;

import org.jetbrains.annotations.NotNull;

public interface CodeProcess {
    void issue(@NotNull Mishap mishap);

    @NotNull Code getCode();

    /**
     * Terminates the process and returns false if it encountered any fatal mishaps,
     * and true if it did not and ran safely.
     */
    boolean finishSilently();

    void finishMaking() throws Maker.MakingException;
}
