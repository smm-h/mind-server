package ir.smmh.apps.plotbot;

import ir.smmh.lingu.Language;
import ir.smmh.lingu.Maker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FigureMaker extends Language, Maker<Figure> {

    void defineConstant(String name, double value);

    @NotNull Iterable<String> getBuiltinOperators();

    @NotNull Iterable<String> getUserDefinedOperators();

    @Nullable Operator getBuiltinOperator(String name);

    @Nullable Operator getUserDefinedOperator(String name);

    void defineOperator(String name, Operator operator);

    boolean forgetUserDefined(String name);

    int forgetAllUserDefined();

    @NotNull Iterable<String> getReservedNames();

    @Nullable String getReservedReasonType(String name);

    @NotNull Iterable<String> getConstants();

    @Nullable Double getConstantValue(String name);

    @NotNull Iterable<String> getVariables();

    @Nullable Double getVariableValue(String name);
}
