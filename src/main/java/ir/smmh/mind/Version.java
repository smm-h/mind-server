package ir.smmh.mind;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public interface Version {
    @NotNull
    LocalDateTime getCreatedOn();
}
