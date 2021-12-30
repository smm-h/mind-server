package ir.smmh.lingu.processors;

public abstract class SingleProcessor implements Processor {

    @Override
    public boolean canBeExtended() {
        return false;
    }

    @Override
    public void extend(Processor processor) {
        throw new UnsupportedOperationException("cannot extend SingleProcessor");
    }

    @Override
    public int getSize() {
        return 1;
    }
}