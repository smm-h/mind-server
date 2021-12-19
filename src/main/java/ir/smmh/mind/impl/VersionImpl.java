package ir.smmh.mind.impl;

import ir.smmh.mind.Version;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class VersionImpl implements Version {
    private final LocalDateTime createdOn;

    public VersionImpl(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public @NotNull LocalDateTime getCreatedOn() {
        return createdOn;
    }
}
