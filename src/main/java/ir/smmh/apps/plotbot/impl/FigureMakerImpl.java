package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.*;
import ir.smmh.lingu.Code;
import ir.smmh.lingu.Mishap;
import ir.smmh.lingu.Token;
import ir.smmh.lingu.impl.CodeImpl;
import ir.smmh.lingu.impl.LanguageImpl;
import ir.smmh.lingu.impl.TokenizerImpl;
import ir.smmh.lingu.impl.TokenizerMakerImpl;
import ir.smmh.lingu.processors.impl.MultiprocessorImpl;
import ir.smmh.util.GraphicsUtil;
import ir.smmh.util.Map;
import ir.smmh.util.NumberPredicates;
import ir.smmh.util.impl.MapImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FigureMakerImpl extends LanguageImpl implements FigureMaker {

    static class ShadeExpression implements Expression {
        private final Expression amount;
        private Expression wrappedExpression;
        private final String color;

        public ShadeExpression(Expression wrappedExpression, String color, Expression amount) {
            this.amount = amount;
            this.color = color;
            this.wrappedExpression = wrappedExpression;
        }

        public Expression amount() {
            return amount;
        }

        public Expression wrappedExpression() {
            return wrappedExpression;
        }

        public String color() {
            return color;
        }

        public void setWrappedExpression(Expression expr) {
            wrappedExpression = expr;
        }

        @Override
        public double evaluate(double x) {
            throw new RuntimeException("HONK!!! This should not be invoked");
        }
    }

    private final Map.SingleValue.Mutable<String, Operator> builtinOps = new MapImpl.SingleValue.Mutable<>();
    private final Map.SingleValue.Mutable<String, Operator> userDefinedOps = new MapImpl.SingleValue.Mutable<>();
    private final Map.SingleValue.Mutable<String, Identifier> identifiers = new MapImpl.SingleValue.Mutable<>();
    private final Stack<Expression> stack = new Stack<>();
    private final Stack<String> debug = new Stack<>();
    private final Set<String> reserved = new HashSet<>();
    private final MarkupWriter markup = MarkupWriter.getInstance();
    private final PlotByXBotStyles styles = PlotByXBotStyles.getInstance();
    private String color = "black";
    private String stroke = "thin";
    private double alpha = 0.7;
    private ShadeExpression savedShadeExpression = null;

    Expression popAndUnwrapIfNeeded() {
        var expr = stack.pop();
        if (expr instanceof ShadeExpression) {
            savedShadeExpression = (ShadeExpression) expr;
            expr = ((ShadeExpression) expr).wrappedExpression();
        }

        assert !(expr instanceof ShadeExpression);
        return expr;
    }

    void pushAndWrapIfNeeded(Expression expr) {
        if (savedShadeExpression != null) {
            savedShadeExpression.setWrappedExpression(expr);
            expr = savedShadeExpression;
            savedShadeExpression = null;
        }
        stack.push(expr);
    }

    @SuppressWarnings("SpellCheckingInspection")
    public FigureMakerImpl() {
        super("PlotLang", "plt", new MultiprocessorImpl());
        TokenizerImpl tokenizer = new TokenizerImpl();
        tokenizer.define(new TokenizerMakerImpl.Streak("whitespace", " \n\t\r"));
        tokenizer.ignore("whitespace");
        tokenizer.define(new TokenizerMakerImpl.Streak("id", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz:"));
        tokenizer.define(new TokenizerMakerImpl.Streak("number", "0123456789."));
        getProcessor().extend(tokenizer);
        reserved.add("x");
        reserved.add("define");
        reserved.add("assign");
        defineBuiltinOperator("x", (Constant) () -> (x -> x));
        defineBuiltinOperator("e", (Constant) () -> (x -> 2.7182818284590452353602874713527));
        defineBuiltinOperator("pi", (Constant) () -> (x -> 3.1415926535897932384626433832795));
        defineBuiltinOperator("phi", (Constant) () -> (x -> 1.6180339887498948482045868343656));
        defineBuiltinOperator("neg", (UnaryOperator) (arg) -> (x -> -arg.evaluate(x)));
        defineBuiltinOperator("abs", (UnaryOperator) (arg) -> (x -> Math.abs(arg.evaluate(x))));
        defineBuiltinOperator("sin", (UnaryOperator) (arg) -> (x -> Math.sin(arg.evaluate(x))));
        defineBuiltinOperator("cos", (UnaryOperator) (arg) -> (x -> Math.cos(arg.evaluate(x))));
        defineBuiltinOperator("tan", (UnaryOperator) (arg) -> (x -> Math.tan(arg.evaluate(x))));
        defineBuiltinOperator("asin", (UnaryOperator) (arg) -> (x -> Math.asin(arg.evaluate(x))));
        defineBuiltinOperator("acos", (UnaryOperator) (arg) -> (x -> Math.acos(arg.evaluate(x))));
        defineBuiltinOperator("atan", (UnaryOperator) (arg) -> (x -> Math.atan(arg.evaluate(x))));
        defineBuiltinOperator("sinh", (UnaryOperator) (arg) -> (x -> Math.sinh(arg.evaluate(x))));
        defineBuiltinOperator("cosh", (UnaryOperator) (arg) -> (x -> Math.cosh(arg.evaluate(x))));
        defineBuiltinOperator("tanh", (UnaryOperator) (arg) -> (x -> Math.tanh(arg.evaluate(x))));
        defineBuiltinOperator("sqrt", (UnaryOperator) (arg) -> (x -> Math.sqrt(arg.evaluate(x))));
        defineBuiltinOperator("cbrt", (UnaryOperator) (arg) -> (x -> Math.cbrt(arg.evaluate(x))));
        defineBuiltinOperator("floor", (UnaryOperator) (arg) -> (x -> Math.floor(arg.evaluate(x))));
        defineBuiltinOperator("round", (UnaryOperator) (arg) -> (x -> Math.round(arg.evaluate(x))));
        defineBuiltinOperator("ceil", (UnaryOperator) (arg) -> (x -> Math.ceil(arg.evaluate(x))));
        defineBuiltinOperator("sgn", (UnaryOperator) (arg) -> (x -> Math.signum(arg.evaluate(x))));
        defineBuiltinOperator("random", (Constant) () -> (x -> Math.random()));
        defineBuiltinOperator("exp", (UnaryOperator) (arg) -> (x -> Math.exp(arg.evaluate(x))));
        defineBuiltinOperator("log", (UnaryOperator) (arg) -> (x -> Math.log(arg.evaluate(x))));
        defineBuiltinOperator("min", (BinaryOperator) (lhs, rhs) -> (x -> Math.min(lhs.evaluate(x), rhs.evaluate(x))));
        defineBuiltinOperator("max", (BinaryOperator) (lhs, rhs) -> (x -> Math.max(lhs.evaluate(x), rhs.evaluate(x))));
        defineBuiltinOperator("add", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) + rhs.evaluate(x)));
        defineBuiltinOperator("sub", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) - rhs.evaluate(x)));
        defineBuiltinOperator("mul", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) * rhs.evaluate(x)));
        defineBuiltinOperator("div", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) / rhs.evaluate(x)));
        defineBuiltinOperator("mod", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) % rhs.evaluate(x)));
        defineBuiltinOperator("pow", (BinaryOperator) (lhs, rhs) -> (x -> Math.pow(lhs.evaluate(x), rhs.evaluate(x))));
    }

    @Override
    public @NotNull Iterable<String> getBuiltinOperators() {
        return builtinOps.overKeys();
    }

    @Override
    public @NotNull Iterable<String> getUserDefinedOperators() {
        return userDefinedOps.overKeys();
    }

    @Override
    public @Nullable Operator getBuiltinOperator(String name) {
        return builtinOps.getAtPlace(name);
    }

    @Override
    public @Nullable Operator getUserDefinedOperator(String name) {
        return userDefinedOps.getAtPlace(name);
    }

    private void defineBuiltinOperator(String name, Operator operator) {
        reserved.add(name);
        builtinOps.setAtPlace(name, operator);
    }

    @Override
    public void defineOperator(String name, Operator operator) {
        userDefinedOps.setAtPlace(name, operator);
    }

    @Override
    public void undefineOperator(String name) {
        userDefinedOps.removeAtPlace(name);
    }

    @Override
    public @NotNull Figure makeFromCode(@NotNull Code code) throws MakingException {
        StringJoiner reports = new StringJoiner("\n");
        List<Figure.Part> figureParts = new ArrayList<>();
        List<Token.Individual> tokens = TokenizerImpl.tokenized.read(code);
//        System.out.println(((Comprehension.List<Token.Individual, String>) token -> token.getTypeString() + ":" + token.getData()).comprehend(tokens));
        java.util.Map<Token.Individual, Set<Mishap>> mishaps = CodeImpl.mishaps.read(code);
        if (mishaps.isEmpty()) {
            if (tokens == null) throw new MakingException("Tokenized port empty");
            stack.clear();
            int index = 0;
            int n = tokens.size();
            while (index < n) {
                Token.Individual token = tokens.get(index++);
                switch (token.getTypeString()) {
                    case "number":
                        double literalValue;
                        try {
                            String data = token.getData();
                            literalValue = Double.parseDouble(data);
                            pushAndWrapIfNeeded(x -> literalValue);
                            debug.push(data);
                        } catch (NumberFormatException e) {
                            throw new MakingException("Could not parse number: " + markup.code(token.getData()));
                        }
                        break;
                    case "id":
                        int arity = 0;
                        String symbol = token.getData();
                        try {
                            if (symbol.equals("assign")) {
                                arity = 2;
                                StringBuilder builder = new StringBuilder();
                                Expression identifier = popAndUnwrapIfNeeded();
                                debug.pop();
                                if (!(identifier instanceof Identifier)) {
                                    throw new MakingException("Cannot assign value to variable using non-identifier expression");
                                }
                                Identifier verifiedIdentifier = (Identifier) identifier;
                                builder.append(verifiedIdentifier.name).append(" = ");
                                double value = popAndUnwrapIfNeeded().evaluate(Double.NaN);
                                builder.append(debug.pop()).append(" = ").append(value);
                                reports.add(markup.code(builder.toString()).getData());
                                if (Double.isNaN(value)) throw new MakingException("Expression evaluation failed");
                                verifiedIdentifier.value = value;
                            } else if (symbol.equals("define")) {
                                arity = 2;
                                StringBuilder builder = new StringBuilder();
                                Expression identifier = popAndUnwrapIfNeeded();
                                debug.pop();
                                if (!(identifier instanceof Identifier)) {
                                    throw new MakingException("Cannot define named function using non-identifier expression");
                                }
                                Identifier verifiedIdentifier = (Identifier) identifier;
                                builder.append(verifiedIdentifier.name);
                                StringJoiner argJoiner = new StringJoiner(", ", "(", ")");
                                final int argCount = (int) popAndUnwrapIfNeeded().evaluate(Double.NaN);
                                debug.pop();
                                Identifier[] boundVariables = new Identifier[argCount];
                                for (int i = 0; i < argCount; i++) {
                                    Expression argIdentifier = popAndUnwrapIfNeeded();
                                    debug.pop();
                                    if (argIdentifier instanceof Identifier) {
                                        Identifier verifiedArgIdentifier = (Identifier) argIdentifier;
                                        argJoiner.add(verifiedArgIdentifier.name);
                                        verifiedArgIdentifier.bind();
                                        boundVariables[i] = verifiedArgIdentifier;
                                    } else {
                                        throw new MakingException("Expression must be an identifier");
                                    }
                                }
                                builder.append(argJoiner).append(" := ");
                                Expression expression = popAndUnwrapIfNeeded();
                                builder.append(debug.pop());
                                reports.add(markup.code(builder.toString()).getData());
                                defineOperator(verifiedIdentifier.name, new Operator() {
                                    @Override
                                    public int getArity() {
                                        return argCount;
                                    }

                                    @Override
                                    public @NotNull Expression makeExpression(Expression... args) {
                                        return x -> {
                                            for (int i = 0; i < argCount; i++) {
                                                boundVariables[i].value = args[i].evaluate(x);
                                            }
                                            return expression.evaluate(x);
                                        };
                                    }
                                });
                            } else if (styles.isColor(symbol)) {
                                color = symbol;
                            } else if (symbol.startsWith(":")) {
                                pushAndWrapIfNeeded(new Identifier(symbol.substring(1), Double.NaN));
                                debug.push(symbol);
                            } else if (symbol.equals("alpha")) {
                                arity = 1;
                                Expression e = popAndUnwrapIfNeeded();
                                debug.pop();
                                alpha = e.evaluate(0);
                                if (alpha > 1 || alpha < 0) {
                                    throw new MakingException("Alpha value must be between 0 and 1");
                                }
                            } else if (styles.isStroke(symbol)) {
                                stroke = symbol;
                            } else if (symbol.equals("and")) {
                                if (stack.isEmpty()) {
                                    throw new MakingException("No expressions were made before " + markup.code("and"));
                                }
                                var expr = stack.pop();
                                if (expr instanceof ShadeExpression) {
                                    var shadeValue = ((ShadeExpression) expr).amount().evaluate(Double.NaN);
                                    color = Integer.toString(GraphicsUtil.multiply(styles.getColor(((ShadeExpression) expr).color()), (int) shadeValue).getRGB());
                                    expr = ((ShadeExpression) expr).wrappedExpression;
                                }
                                figureParts.add(new FigurePartImpl(color, alpha, stroke, debug.pop(), expr));
                                if (!stack.isEmpty()) {
                                    StringBuilder builder = new StringBuilder("Multiple expressions were made before" + markup.code("and") + " :");
                                    while (!stack.isEmpty()) {
                                        popAndUnwrapIfNeeded();
                                        builder.append("\n").append(markup.code(debug.pop()));
                                    }
                                    throw new MakingException(builder.toString());
                                }
                            } else if (symbol.equals("iterate")) {
                                arity = 3;
                                StringBuilder builder = new StringBuilder();
                                builder.append("for ");
                                Expression identifier = popAndUnwrapIfNeeded();
                                debug.pop();
                                if (!(identifier instanceof Identifier)) {
                                    throw new MakingException("Cannot define named function using non-identifier expression");
                                }
                                Identifier verifiedIdentifier = (Identifier) identifier;
                                builder.append(verifiedIdentifier.name);
                                verifiedIdentifier.bind();
                                Expression limitExpression = popAndUnwrapIfNeeded();
                                debug.pop();
                                int limit = (int) limitExpression.evaluate(Double.NaN);
                                Expression expression = stack.pop();
                                String es = debug.pop();
                                builder.append(" in range(").append(limit).append("): ").append(es);
                                for (int i = 0; i < limit; i++) {
                                    int v = i;
                                    var e = expression;
                                    if (e instanceof ShadeExpression) {
                                        verifiedIdentifier.value = i;
                                        var shadeValue = ((ShadeExpression) e).amount().evaluate(Double.NaN);
                                        color = Integer.toString(GraphicsUtil.multiply(styles.getColor(((ShadeExpression) e).color()), (int) shadeValue).getRGB());
                                        e = ((ShadeExpression) e).wrappedExpression;
                                    }
                                    final var ex = e;
                                    Expression ei = x -> {
                                        verifiedIdentifier.value = v;
                                        return ex.evaluate(x);
                                    };
                                    figureParts.add(new FigurePartImpl(color, alpha, stroke, es + " where i=" + i, ei));
                                }
                                reports.add(markup.code(builder.toString()).getData());
                            } else if (symbol.equals("shade")) {
                                var color = popAndUnwrapIfNeeded();
                                debug.pop();
                                if (!(color instanceof Identifier))
                                    throw new MakingException("Shade color must be an identifier, but it was a " + color.getClass().getSimpleName() + " reeeee");
                                var colorName = ((Identifier) color).name;

                                var shadeValue = new Expression() {
                                    private final Expression expression = popAndUnwrapIfNeeded();

                                    @Override
                                    public double evaluate(double x) {
                                        var shadeValue = expression.evaluate(x);
                                        if (!NumberPredicates.WHOLE.test(shadeValue) || 0 > shadeValue || shadeValue > 255)
                                            return Double.NaN;

                                        if (Double.isNaN(shadeValue))
                                            return Double.NaN;

                                        return shadeValue;
                                    }
                                };

                                debug.pop();
                                pushAndWrapIfNeeded(new ShadeExpression(popAndUnwrapIfNeeded(), colorName, shadeValue));
                            } else {
                                Operator op = builtinOps.getAtPlace(symbol);
                                if (op == null) op = userDefinedOps.getAtPlace(symbol);
                                if (op == null) {
                                    Identifier v = identifiers.getAtPlace(symbol);
                                    if (v == null) {
                                        v = new Identifier(symbol, Double.NaN);
                                    }
                                    pushAndWrapIfNeeded(v);
                                    debug.push(v.name);
                                } else {
                                    arity = op.getArity();
                                    Expression[] args = new Expression[arity];
                                    if (arity > 0) {
                                        StringBuilder builder = new StringBuilder();
                                        for (int i = 0; i < arity; i++) {
                                            args[arity - 1 - i] = popAndUnwrapIfNeeded();
                                            if (i > 0) builder.insert(0, ", ");
                                            builder.insert(0, debug.pop());
                                        }
                                        debug.push(symbol + "(" + builder + ")");
                                    } else {
                                        debug.push(symbol);
                                    }
                                    pushAndWrapIfNeeded(op.makeExpression(args));
                                }
                            }
                        } catch (EmptyStackException e) {
                            throw new MakingException("Not enough arguments for operation: " + markup.code(symbol) + ", needs " + arity);
                        }
                        break;
                    default:
                        throw new MakingException("Unhandled token type: " + markup.code(token.getTypeString()) + "");
                }
            }
            if (!stack.isEmpty()) {
                var expr = stack.pop();
                if (expr instanceof ShadeExpression) {
                    var shadeValue = ((ShadeExpression) expr).amount().evaluate(Double.NaN);
                    color = Integer.toString(GraphicsUtil.multiply(styles.getColor(((ShadeExpression) expr).color()), (int) shadeValue).getRGB());
                    expr = ((ShadeExpression) expr).wrappedExpression;
                }
                figureParts.add(new FigurePartImpl(color, alpha, stroke, debug.pop(), expr));
                if (!stack.isEmpty()) {
                    StringBuilder builder = new StringBuilder("Multiple expressions were made before" + markup.code("and") + " :");
                    while (!stack.isEmpty()) {
                        popAndUnwrapIfNeeded();
                        builder.append("\n").append(markup.code(debug.pop()));
                    }
                    throw new MakingException(builder.toString());
                }
            }
            if (figureParts.isEmpty()) {
                reports.add("No expressions were made");
            }
            return new FigureImpl(figureParts, reports.toString());
        } else {
            for (Set<Mishap> set : mishaps.values()) {
                for (Mishap mishap : set) {
                    reports.add(mishap.getReport());
                }
            }
            throw new MakingException(reports.toString());
        }
    }

    private class Identifier implements Expression {
        final String name;
        double value;

        private Identifier(String name, double value) throws MakingException {
            if (reserved.contains(name))
                throw new MakingException("Cannot reuse reserved identifier: " + markup.code(name));
            identifiers.setAtPlace(name, this);
            this.name = name;
            this.value = value;
        }

        @Override
        public double evaluate(double x) {
            if (Double.isNaN(value)) throw new NullPointerException("Variable is not assigned: " + markup.code(name));
            return value;
        }

        private void bind() throws MakingException {
            if (Double.isNaN(value)) {
                identifiers.removeAtPlace(name);
            } else {
                throw new MakingException("Cannot use free identifier as bound:" + markup.code(name));
            }
        }
    }
}
