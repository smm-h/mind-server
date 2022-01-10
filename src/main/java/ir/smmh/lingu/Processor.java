package ir.smmh.lingu;

public interface Processor {

    /**
     * Only ever call this inside another {@code Processor#process} overriding or
     * the {@code CodeImpl#beProcessed}.
     */
    void process(Code code);

    boolean canBeExtended();

    void extend(Processor processor) throws UnsupportedOperationException;

    int getSize();
}
