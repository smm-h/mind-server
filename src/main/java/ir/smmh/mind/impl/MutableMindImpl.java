package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import org.jetbrains.annotations.Nullable;

public class MutableMindImpl implements Mind.Mutable {

    @Override
    public @Nullable Idea.Mutable imagine(String name, boolean create) {
        return null;
    }

    @Override
    public Immutable freeze() {
        return null;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void taint() {

    }

    @Override
    public void onClean() {

    }
}
