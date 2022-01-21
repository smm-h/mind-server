package ir.smmh.util.impl;

import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.Listeners;
import ir.smmh.util.ListenersImpl;
import ir.smmh.util.Mutable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class MutableImpl implements Mutable.WithListeners {

    private final Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>>
            onCleanListeners = ListenersImpl.blank();
    private final Listeners<FunctionalUtil.OnEventListener>
            onPostMutateListeners = ListenersImpl.blank(),
            onPreMutateListeners = ListenersImpl.blank();
    private boolean dirty = true;

//    private static final ReferenceQueue<Mutable> q = new ReferenceQueue<>();

    private MutableImpl() {
        super();
    }

    public static Mutable.WithListeners blank() {
        return new MutableImpl();
    }

    @Override
    public boolean isDirty() {
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
