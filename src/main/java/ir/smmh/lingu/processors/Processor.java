package ir.smmh.lingu.processors;

import ir.smmh.lingu.Code;
import ir.smmh.lingu.Linter;

public interface Processor extends Linter {

    /**
     * Only ever call this inside another {@link Processor#process} overriding or
     * the {@link Code#beProcessed}.
     */
    void process(Code code);

    boolean canBeExtended();

    void extend(Processor processor) throws UnsupportedOperationException;

    int getSize();
}
