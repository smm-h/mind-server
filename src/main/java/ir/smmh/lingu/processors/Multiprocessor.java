package ir.smmh.lingu.processors;

import ir.smmh.lingu.Processor;

public interface Multiprocessor extends Processor {

    /**
     * Abstract classes must not call this so they can be extended.
     */
    void seal();
}
