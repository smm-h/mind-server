package ir.smmh.lingu.processors;

import ir.smmh.lingu.Processor;
import org.jetbrains.annotations.NotNull;

public abstract class SingleProcessor implements Processor {

    @Override
    public boolean canBeExtended() {
        return false;
    }

    @Override
    public void extend(@NotNull Processor processor) {
        throw new UnsupportedOperationException("cannot extend SingleProcessor");
    }

    @Override
    public int getSize() {
        return 1;
    }
}