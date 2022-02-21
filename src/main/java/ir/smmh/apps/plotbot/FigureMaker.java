package ir.smmh.apps.plotbot;

import ir.smmh.lingu.Language;
import ir.smmh.lingu.Maker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FigureMaker extends Language/*, Maker<Figure>*/ {

    @NotNull Figure make(long chatId, String text) throws Maker.MakingException;

    void defineConstant(String name, double value);

    @NotNull Iterable<String> getBuiltinOperators();

    @Nullable Operator getBuiltinOperator(String name);

    @NotNull Iterable<String> getReservedNames();

    @Nullable String getReservedReasonType(String name);

    @NotNull Iterable<String> getConstants();

    @Nullable Double getConstantValue(String name);
}
