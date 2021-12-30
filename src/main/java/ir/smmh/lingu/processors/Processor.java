package ir.smmh.lingu.processors;

import ir.smmh.lingu.Linter;
import ir.smmh.lingu.impl.CodeImpl;

public interface Processor extends Linter {

    /**
     * Only ever call this inside another {@link Processor#process} overriding or
     * the {@link CodeImpl#beProcessed}.
     */
    void process(CodeImpl code);

    boolean canBeExtended();

    void extend(Processor processor) throws UnsupportedOperationException;

    int getSize();
}
