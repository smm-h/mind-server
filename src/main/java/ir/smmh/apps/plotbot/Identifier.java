package ir.smmh.apps.plotbot;

import ir.smmh.lingu.Maker;
import org.jetbrains.annotations.NotNull;

public interface Identifier extends Expression {
    @NotNull String getName();

    double getValue();

    void setValue(double value);

    void bind() throws Maker.MakingException;
}
