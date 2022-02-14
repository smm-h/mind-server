package ir.smmh.apps.plotbot;

import ir.smmh.lingu.Language;
import ir.smmh.lingu.Maker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlotParser extends Language, Maker<Expression> {

    @NotNull Expression parse(String string) throws MakingException;

    @NotNull Iterable<String> getBuiltinOperators();

    @NotNull Iterable<String> getUserDefinedOperators();

    @Nullable Operator getBuiltinOperator(String name);

    @Nullable Operator getUserDefinedOperator(String name);

    void defineOperator(String name, Operator operator);

    void undefineOperator(String name);

    @FunctionalInterface
    interface Constant extends Operator {
        @NotNull Expression makeNullaryExpression();

        @Override
        default int getArity() {
            return 0;
        }
    }

    @FunctionalInterface
    interface UnaryOperator extends Operator {
        @NotNull Expression makeUnaryExpression(Expression arg);

        @Override
        default int getArity() {
            return 1;
        }
    }

    @FunctionalInterface
    interface BinaryOperator extends Operator {
        @NotNull Expression makeBinaryExpression(Expression lhs, Expression rhs);

        @Override
        default int getArity() {
            return 2;
        }
    }
}
