package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.Expression;
import ir.smmh.apps.plotbot.Figure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FigurePartImpl implements Figure.Part {

    private final @NotNull Expression expression;
    private final @Nullable String title;
    private final @Nullable String color;
    private final @Nullable String stroke;

    public FigurePartImpl(@NotNull Expression expression, @Nullable String title, @Nullable String color, @Nullable String stroke) {
        this.expression = expression;
        this.title = title;
        this.color = color;
        this.stroke = stroke;
    }

    @Override
    public @NotNull Expression getExpression() {
        return expression;
    }

    @Override
    public @Nullable String getTitle() {
        return title;
    }

    @Override
    public @Nullable String getColor() {
        return color;
    }

    @Override
    public @Nullable String getStroke() {
        return stroke;
    }
}
