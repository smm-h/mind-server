package ir.smmh.lingu;

public interface CodeProcess {
    void issue(Mishap mishap);

    /**
     * Terminates the process and returns false if it encountered any fatal mishaps,
     * and true if it did not and ran safely.
     */
    boolean finish();
}
