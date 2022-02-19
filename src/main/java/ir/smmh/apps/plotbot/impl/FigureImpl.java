package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.Figure;
import ir.smmh.apps.plotbot.Viewport;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FigureImpl implements Figure {
    private final List<Part> parts;
    private final String report;
    private final Viewport viewport;

    public FigureImpl(List<Part> parts, String report, Viewport viewport) {
        this.parts = parts;
        this.report = report;
        this.viewport = viewport;
    }

    @Override
    public @NotNull Iterable<Part> getParts() {
        return parts;
    }

    @Override
    public int getPartCount() {
        return parts.size();
    }

    @Override
    public @NotNull String getReport() {
        return report;
    }

    @Override
    public @NotNull Viewport getViewport() {
        return viewport;
    }
}
