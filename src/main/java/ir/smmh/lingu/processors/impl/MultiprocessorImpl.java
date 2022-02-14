package ir.smmh.lingu.processors.impl;

import ir.smmh.lingu.Code;
import ir.smmh.lingu.Processor;
import ir.smmh.lingu.processors.Multiprocessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MultiprocessorImpl implements Multiprocessor {

    private final List<Processor> processors = new ArrayList<>();

    private boolean sealed = false;

    public MultiprocessorImpl(Processor... processors) {
        for (Processor processor : processors) {
            extend(processor);
        }
    }

    @Override
    public void seal() {
        sealed = true;
    }

    @Override
    public final void process(@NotNull Code code) {
        for (Processor processor : processors)
            processor.process(code);
    }

    @Override
    public boolean canBeExtended() {
        return !sealed;
    }

    @Override
    public void extend(@NotNull Processor processor) {
        if (canBeExtended()) {
            processors.add(processor);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public int getSize() {
        return processors.size();
    }
}