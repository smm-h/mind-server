package ir.smmh.util.impl;

import ir.smmh.util.Mutable;

public class MutableImpl implements Mutable {

    private boolean dirty = true;

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void taint() {
        dirty = true;
    }

    @Override
    public void onClean() {
        dirty = false;
    }
}
