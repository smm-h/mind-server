package ir.smmh.apps.plotbot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Figure {
    @NotNull List<Part> getParts();

    int getPartCount();

    @NotNull String getReport();

    @NotNull Viewport getViewport();

    interface Part {
        @NotNull Expression getExpression();

        @Nullable String getTitle();

        @Nullable String getColor();

        @Nullable String getStroke();
    }
}
