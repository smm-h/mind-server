package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.Figure;

import java.util.List;

public class FigureImpl implements Figure {
    private final List<Part> parts;
    private final String report;

    public FigureImpl(List<Part> parts, String report) {
        this.parts = parts;
        this.report = report;
    }

    @Override
    public Iterable<Part> getParts() {
        return parts;
    }

    @Override
    public int getPartCount() {
        return parts.size();
    }

    @Override
    public String getReport() {
        return report;
    }
}
