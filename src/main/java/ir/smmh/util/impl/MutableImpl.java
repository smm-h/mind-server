package ir.smmh.util.impl;

import ir.smmh.util.Mutable;

import java.util.LinkedList;
import java.util.List;

public class MutableImpl implements Mutable {

    private boolean dirty = true;
    private final List<OnCleanListener> listeners = new LinkedList<>();

    @Override
    public final boolean isDirty() {
        return dirty;
    }

    @Override
    public final void taint() {
        dirty = true;
    }

    @Override
    public final void onClean() {
        dirty = false;
        for (OnCleanListener listener : listeners)
            listener.onClean();
    }

    @Override
    public void addOnCleanListener(OnCleanListener listener) {
        listeners.add(listener);
    }
}
