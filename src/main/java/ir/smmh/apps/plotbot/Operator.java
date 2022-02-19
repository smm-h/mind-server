package ir.smmh.apps.plotbot;

import ir.smmh.lingu.Maker;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public
interface Operator {

    default String getType() {
        return "Multiary Operator";
    }

    int getArity();

    default @NotNull Expression makeExpression(Expression... args) throws Maker.MakingException {
        if (this instanceof Nullary) {
            return ((Nullary) this).makeNullaryExpression();
        } else if (this instanceof Unary) {
            return ((Unary) this).makeUnaryExpression(args[0]);
        } else if (this instanceof Binary) {
            return ((Binary) this).makeBinaryExpression(args[0], args[1]);
        } else {
            throw new Maker.MakingException("override makeExpression in your operator");
        }
    }

    @FunctionalInterface
    interface Nullary extends Operator {
        default String getType() {
            return "Nullary Operator";
        }

        @NotNull Expression makeNullaryExpression();

        @Override
        default int getArity() {
            return 0;
        }
    }

    @FunctionalInterface
    interface Unary extends Operator {
        default String getType() {
            return "Unary Operator";
        }

        @NotNull Expression makeUnaryExpression(Expression arg);

        @Override
        default int getArity() {
            return 1;
        }
    }

    @FunctionalInterface
    interface Binary extends Operator {
        default String getType() {
            return "Binary Operator";
        }

        @NotNull Expression makeBinaryExpression(Expression lhs, Expression rhs);

        @Override
        default int getArity() {
            return 2;
        }
    }
}
