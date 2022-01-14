package ir.smmh.util.impl;

import ir.smmh.util.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Stack;

@ParametersAreNonnullByDefault
public class MutableImpl implements Mutable {

    private static final Stack<MutableImpl> stack = new Stack<>();
    private final Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>>
            onCleanListeners = new ListenersImpl<>();
    private final Listeners<FunctionalUtil.OnEventListener>
            onPostMutateListeners = new ListenersImpl<>(),
            onPreMutateListeners = new ListenersImpl<>();
    private final Mutable.Injected injected;
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
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPreMutateListeners() {
        return onPreMutateListeners;
    }

    @Override
    public @NotNull Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>> getOnCleanListeners() {
        return onCleanListeners;
    }

    @Override
    public @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPostMutateListeners() {
        return onPostMutateListeners;
    }
}
