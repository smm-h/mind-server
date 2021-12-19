package ir.smmh.mind.impl;

import ir.smmh.mind.Mind;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class ImmutableMindImpl implements Mind.Immutable {
    private final LocalDateTime createdOn;

    public ImmutableMindImpl(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public @NotNull LocalDateTime getCreatedOn() {
        return createdOn;
    }
}
