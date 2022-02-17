package ir.smmh.apps.plotbot;

import ir.smmh.lingu.Maker;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public
interface Operator {

    default String getType() {
        switch (getArity()) {
            case 0:
                return "Constant";
            case 1:
                return "Unary Operator";
            case 2:
                return "Binary Operator";
            default:
                return "Multiary Operator";
        }
    }

    int getArity();

    default @NotNull Expression makeExpression(Expression... args) throws Maker.MakingException {
        if (this instanceof FigureMaker.Constant) {
            return ((FigureMaker.Constant) this).makeNullaryExpression();
        } else if (this instanceof FigureMaker.UnaryOperator) {
            return ((FigureMaker.UnaryOperator) this).makeUnaryExpression(args[0]);
        } else if (this instanceof FigureMaker.BinaryOperator) {
            return ((FigureMaker.BinaryOperator) this).makeBinaryExpression(args[0], args[1]);
        } else {
            throw new Maker.MakingException("override makeExpression in your operator");
        }
    }
}
