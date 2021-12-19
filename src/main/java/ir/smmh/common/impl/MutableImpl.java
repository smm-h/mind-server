package ir.smmh.common.impl;

import ir.smmh.common.Mutable;

public abstract class MutableImpl<T> implements Mutable<T> {

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
