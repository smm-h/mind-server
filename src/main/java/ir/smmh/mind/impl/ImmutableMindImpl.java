package ir.smmh.mind.impl;

import ir.smmh.mind.Idea;
import ir.smmh.mind.Mind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public class ImmutableMindImpl implements Mind.Immutable {
    @Nullable
    @Override
    public Idea.Immutable find(String name) {
        return null;
    }
}
