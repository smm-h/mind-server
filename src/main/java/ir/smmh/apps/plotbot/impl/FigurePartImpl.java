package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.Expression;
import ir.smmh.apps.plotbot.Figure;

public class FigurePartImpl implements Figure.Part {

    private final String color;
    private final String stroke;
    private final String title;
    private final Expression expression;
    private final double opacity;

    public FigurePartImpl(String color, double opacity, String stroke, String title, Expression expression) {
        this.color = color;
        this.opacity = opacity;
        this.stroke = stroke;
        this.title = title;
        this.expression = expression;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public double getAlpha() {
        return opacity;
    }

    @Override
    public String getStroke() {
        return stroke;
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
