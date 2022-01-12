package ir.smmh.util.impl;

import ir.smmh.util.Mutable;
import ir.smmh.util.Named;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class MutableImpl implements Mutable {

    private static final Stack<MutableImpl> stack = new Stack<>();
    private final List<OnCleanListener> listeners = new LinkedList<>();
    private final Object injected;
    private boolean dirty = true;

    public MutableImpl(Mutable.Injected injected) {
        this.injected = injected;
        stack.push(this);
    }

    public static void cleanEverything() {
        while (!stack.isEmpty()) {
            MutableImpl mutable = stack.pop();
            String name = Named.nameOf(mutable.injected);
            System.out.println("CLEANING: " + name);
            try {
                mutable.clean();
            } catch (Throwable e) {
                System.err.println("FAILED TO CLEAN: " + name);
            }
        }
    }

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
        for (OnCleanListener listener : listeners) {
            try {
                listener.onClean();
            } catch (CleaningException e) {
                taint();
            }
        }
    }

    @Override
    public void addOnCleanListener(OnCleanListener listener) {
        listeners.add(listener);
    }
}
