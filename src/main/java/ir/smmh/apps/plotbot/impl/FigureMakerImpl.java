package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.*;
import ir.smmh.apps.plotbot.Operator.Binary;
import ir.smmh.apps.plotbot.Operator.Nullary;
import ir.smmh.apps.plotbot.Operator.Unary;
import ir.smmh.lingu.Code;
import ir.smmh.lingu.Maker.MakingException;
import ir.smmh.lingu.Mishap;
import ir.smmh.lingu.Token;
import ir.smmh.lingu.impl.CodeImpl;
import ir.smmh.lingu.impl.LanguageImpl;
import ir.smmh.lingu.impl.TokenizerImpl;
import ir.smmh.lingu.impl.TokenizerMakerImpl;
import ir.smmh.lingu.processors.impl.MultiprocessorImpl;
import ir.smmh.util.Map;
import ir.smmh.util.impl.MapImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static ir.smmh.util.FunctionalUtil.with;

public class FigureMakerImpl extends LanguageImpl implements FigureMaker {

    private static final int MAX_ITERATION_LIMIT = 1000;
    private final Map.SingleValue.Mutable<String, Operator> builtinOps = new MapImpl.SingleValue.Mutable<>();
    private final Map.SingleValue.Mutable<String, Expression> constants = new MapImpl.SingleValue.Mutable<>();
    private final Stack<Expression> stack = new Stack<>();
    private final Stack<String> debug = new Stack<>();
    private final java.util.Map<String, ReservationReason> reserved = new HashMap<>();
    private final MarkupWriter markup = MarkupWriter.getInstance();
    private final Styles styles = Styles.getInstance();
    private String color = null, stroke = null;

    @SuppressWarnings("SpellCheckingInspection")
    public FigureMakerImpl() {
        super("PlotLang", "plt", new MultiprocessorImpl());
        TokenizerImpl tokenizer = new TokenizerImpl();
        tokenizer.define(new TokenizerMakerImpl.Streak("whitespace", " \n\t\r"));
        tokenizer.ignore("whitespace");
        tokenizer.define(new TokenizerMakerImpl.Streak("id", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"));
        tokenizer.define(new TokenizerMakerImpl.Streak("number", "0123456789."));
        getProcessor().extend(tokenizer);
        reserved.put("define", ReservationReason.KEYWORD);
        reserved.put("assign", ReservationReason.KEYWORD);
        reserved.put("and", ReservationReason.KEYWORD);
        reserved.put("iterate", ReservationReason.KEYWORD);
        reserved.put("viewport", ReservationReason.KEYWORD);
        reserved.put("thickness", ReservationReason.KEYWORD);
        for (String name : styles.getColorNames()) reserved.put(name, ReservationReason.COLOR);
        for (String name : styles.getStrokeNames()) reserved.put(name, ReservationReason.STROKE);
        defineConstant("e", 2.7182818284590452353602874713527);
        defineConstant("pi", 3.1415926535897932384626433832795);
        defineConstant("phi", 1.6180339887498948482045868343656);
        defineConstant("infinity", Double.POSITIVE_INFINITY);
        defineConstant("epsilon", Double.MIN_VALUE);
        defineBuiltinOperator("x", (Nullary) () -> (x -> x));
        defineBuiltinOperator("random", (Nullary) () -> (x -> Math.random()));
        defineBuiltinOperator("neg", (Unary) (arg) -> (x -> -arg.evaluate(x)));
        defineBuiltinOperator("abs", (Unary) (arg) -> (x -> Math.abs(arg.evaluate(x))));
        defineBuiltinOperator("sin", (Unary) (arg) -> (x -> Math.sin(arg.evaluate(x))));
        defineBuiltinOperator("cos", (Unary) (arg) -> (x -> Math.cos(arg.evaluate(x))));
        defineBuiltinOperator("tan", (Unary) (arg) -> (x -> Math.tan(arg.evaluate(x))));
        defineBuiltinOperator("asin", (Unary) (arg) -> (x -> Math.asin(arg.evaluate(x))));
        defineBuiltinOperator("acos", (Unary) (arg) -> (x -> Math.acos(arg.evaluate(x))));
        defineBuiltinOperator("atan", (Unary) (arg) -> (x -> Math.atan(arg.evaluate(x))));
        defineBuiltinOperator("sinh", (Unary) (arg) -> (x -> Math.sinh(arg.evaluate(x))));
        defineBuiltinOperator("cosh", (Unary) (arg) -> (x -> Math.cosh(arg.evaluate(x))));
        defineBuiltinOperator("tanh", (Unary) (arg) -> (x -> Math.tanh(arg.evaluate(x))));
        defineBuiltinOperator("sqrt", (Unary) (arg) -> (x -> Math.sqrt(arg.evaluate(x))));
        defineBuiltinOperator("cbrt", (Unary) (arg) -> (x -> Math.cbrt(arg.evaluate(x))));
        defineBuiltinOperator("floor", (Unary) (arg) -> (x -> Math.floor(arg.evaluate(x))));
        defineBuiltinOperator("round", (Unary) (arg) -> (x -> Math.round(arg.evaluate(x))));
        defineBuiltinOperator("ceil", (Unary) (arg) -> (x -> Math.ceil(arg.evaluate(x))));
        defineBuiltinOperator("sgn", (Unary) (arg) -> (x -> Math.signum(arg.evaluate(x))));
        defineBuiltinOperator("exp", (Unary) (arg) -> (x -> Math.exp(arg.evaluate(x))));
        defineBuiltinOperator("log", (Unary) (arg) -> (x -> Math.log(arg.evaluate(x))));
        defineBuiltinOperator("min", (Binary) (lhs, rhs) -> (x -> Math.min(lhs.evaluate(x), rhs.evaluate(x))));
        defineBuiltinOperator("max", (Binary) (lhs, rhs) -> (x -> Math.max(lhs.evaluate(x), rhs.evaluate(x))));
        defineBuiltinOperator("add", (Binary) (lhs, rhs) -> (x -> lhs.evaluate(x) + rhs.evaluate(x)));
        defineBuiltinOperator("sub", (Binary) (lhs, rhs) -> (x -> lhs.evaluate(x) - rhs.evaluate(x)));
        defineBuiltinOperator("mul", (Binary) (lhs, rhs) -> (x -> lhs.evaluate(x) * rhs.evaluate(x)));
        defineBuiltinOperator("div", (Binary) (lhs, rhs) -> (x -> lhs.evaluate(x) / rhs.evaluate(x)));
        defineBuiltinOperator("mod", (Binary) (lhs, rhs) -> (x -> lhs.evaluate(x) % rhs.evaluate(x)));
        defineBuiltinOperator("pow", (Binary) (lhs, rhs) -> (x -> Math.pow(lhs.evaluate(x), rhs.evaluate(x))));
    }

    @Override
    public void defineConstant(String name, double value) {
        reserved.put(name, ReservationReason.CONSTANT);
        constants.setAtPlace(name, x -> value);
    }

    @Override
    public @NotNull Iterable<String> getBuiltinOperators() {
        return builtinOps.overKeys();
    }

    @Override
    public @Nullable Operator getBuiltinOperator(String name) {
        return builtinOps.getAtPlace(name);
    }

    private void defineBuiltinOperator(String name, Operator operator) {
        reserved.put(name, ReservationReason.OPERATOR);
        builtinOps.setAtPlace(name, operator);
    }

    @Override
    public @NotNull Iterable<String> getReservedNames() {
        return reserved.keySet();
    }

    @Override
    public @Nullable String getReservedReasonType(String name) {
        return reserved.get(name).type();
    }

    @Override
    public @NotNull Iterable<String> getConstants() {
        return constants.overKeys();
    }

    @Override
    public @Nullable Double getConstantValue(String name) {
        return with(constants.getAtPlace(name), c -> c.evaluate(Double.NaN), null);
    }

    @Override
    public @NotNull Figure make(long chatId, String text) throws MakingException {
        UserData ud = PlotByXBot.getInstance().getUser(chatId);
        StringJoiner reports = new StringJoiner("\n");
        Viewport viewport = PlotByXBot.getInstance().currentViewport;
        List<Figure.Part> figureParts = new ArrayList<>();
        Code code = new CodeImpl(text, "plt");
        List<Token.Individual> tokens = TokenizerImpl.tokenized.read(code);
//        System.out.println(Comprehend.list(tokens, token -> token.getTypeString() + ":" + token.getData()));
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
                            stack.push(x -> literalValue);
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
                                Expression identifier = stack.pop();
                                debug.pop();
                                if (!(identifier instanceof Identifier)) {
                                    throw new MakingException("Cannot assign value to non-identifier expression");
                                }
                                Identifier verifiedIdentifier = (Identifier) identifier;
                                builder.append(verifiedIdentifier.getName()).append(" = ");
                                double value = stack.pop().evaluate(Double.NaN);
                                builder.append(debug.pop()).append(" = ").append(value);
                                reports.add(markup.code(builder.toString()).getData());
                                if (Double.isNaN(value))
                                    throw new MakingException("Expression evaluation failed");
                                verifiedIdentifier.setValue(value);
                            } else if (symbol.equals("define")) {
                                arity = 2;
                                StringBuilder builder = new StringBuilder();
                                Expression identifier = stack.pop();
                                debug.pop();
                                if (!(identifier instanceof Identifier)) {
                                    throw new MakingException("Cannot define operation with non-identifier expression");
                                }
                                Identifier verifiedIdentifier = (Identifier) identifier;
                                builder.append(verifiedIdentifier.getName());
                                StringJoiner argJoiner = new StringJoiner(", ", "(", ")");
                                final int argCount = (int) stack.pop().evaluate(Double.NaN);
                                debug.pop();
                                Identifier[] boundVariables = new Identifier[argCount];
                                for (int i = 0; i < argCount; i++) {
                                    Expression argIdentifier = stack.pop();
                                    debug.pop();
                                    if (argIdentifier instanceof Identifier) {
                                        Identifier verifiedArgIdentifier = (Identifier) argIdentifier;
                                        argJoiner.add(verifiedArgIdentifier.getName());
                                        verifiedArgIdentifier.bind();
                                        boundVariables[i] = verifiedArgIdentifier;
                                    } else {
                                        throw new MakingException("Expression must be an identifier");
                                    }
                                }
                                builder.append(argJoiner).append(" := ");
                                Expression expression = stack.pop();
                                builder.append(debug.pop());
                                reports.add(markup.code(builder.toString()).getData());
                                ud.defineOperator(verifiedIdentifier.getName(), new Operator() {
                                    @Override
                                    public int getArity() {
                                        return argCount;
                                    }

                                    @Override
                                    public @NotNull Expression makeExpression(Expression... args) {
                                        return x -> {
                                            for (int i = 0; i < argCount; i++) {
                                                boundVariables[i].setValue(args[i].evaluate(x));
                                            }
                                            return expression.evaluate(x);
                                        };
                                    }
                                });
                            } else if (styles.isColor(symbol)) {
                                color = symbol;
                            } else if (styles.isStroke(symbol)) {
                                stroke = symbol;
                            } else if (symbol.equals("thickness")) {
                                debug.pop();
                                double thickness = stack.pop().evaluate(Double.NaN);
                                if (Double.isNaN(thickness))
                                    throw new MakingException("Thickness must not depend on " + markup.code("x"));
                                stroke = "thickness_" + thickness;
                                styles.addStroke(stroke, thickness);
                            } else if (symbol.equals("and")) {
                                if (stack.isEmpty()) {
                                    throw new MakingException("No expressions were made before " + markup.code("and"));
                                }
                                figureParts.add(new FigurePartImpl(stack.pop(), debug.pop(), color, stroke));
                                color = null;
                                stroke = null;
                                if (!stack.isEmpty()) {
                                    StringBuilder builder = new StringBuilder("Multiple expressions were made before " + markup.code("and") + " :");
                                    while (!stack.isEmpty()) {
                                        stack.pop();
                                        builder.append("\n").append(markup.code(debug.pop()));
                                    }
                                    throw new MakingException(builder.toString());
                                }
                            } else if (symbol.equals("iterate")) {
                                arity = 3;
                                StringBuilder builder = new StringBuilder();
                                builder.append("for ");
                                Expression identifier = stack.pop();
                                debug.pop();
                                if (!(identifier instanceof Identifier)) {
                                    throw new MakingException("Cannot bind iteration index to a non-identifier expression");
                                }
                                Identifier verifiedIdentifier = (Identifier) identifier;
                                builder.append(verifiedIdentifier.getName());
                                verifiedIdentifier.bind();
                                Expression limitExpression = stack.pop();
                                debug.pop();
                                int limit = (int) limitExpression.evaluate(Double.NaN);
                                if (limit == 0)
                                    throw new MakingException("Iteration limit cannot be zero");
                                if (limit < 0)
                                    throw new MakingException("Iteration limit cannot be negative");
                                if (limit > MAX_ITERATION_LIMIT)
                                    throw new MakingException("Iteration limit cannot be higher than " + markup.code(Integer.toString(MAX_ITERATION_LIMIT)));
                                Expression e = stack.pop();
                                String es = debug.pop();
                                builder.append(" in range(").append(limit).append("): ").append(es);
                                for (int i = 0; i < limit; i++) {
                                    int v = i;
                                    Expression ei = x -> {
                                        verifiedIdentifier.setValue(v);
                                        return e.evaluate(x);
                                    };
                                    figureParts.add(new FigurePartImpl(ei, null, color, stroke));
                                }
                                reports.add(markup.code(builder.toString()).getData());
                            } else if (symbol.equals("viewport")) {
                                arity = 4;
                                debug.pop();
                                double vr = stack.pop().evaluate(Double.NaN);
                                if (Double.isNaN(vr))
                                    throw new MakingException("Vertical radius must not depend on " + markup.code("x"));
                                debug.pop();
                                double hr = stack.pop().evaluate(Double.NaN);
                                if (Double.isNaN(hr))
                                    throw new MakingException("Horizontal radius must not depend on " + markup.code("x"));
                                debug.pop();
                                double oy = stack.pop().evaluate(Double.NaN);
                                if (Double.isNaN(oy))
                                    throw new MakingException("Center Y must not depend on " + markup.code("x"));
                                debug.pop();
                                double ox = stack.pop().evaluate(Double.NaN);
                                if (Double.isNaN(ox))
                                    throw new MakingException("Center X must not depend on " + markup.code("x"));
                                viewport = ViewportImpl.make(ox, oy, hr, vr);
                            } else if (constants.containsPlace(symbol)) {
                                stack.push(constants.getAtPlace(symbol));
                                debug.push(symbol);
                            } else {
                                Operator op = builtinOps.getAtPlace(symbol);
                                if (op == null) op = ud.getUserDefinedOperator(symbol);
                                if (op == null) {
                                    Identifier identifier = ud.getVariableIdentifier(symbol);
                                    if (identifier == null) {
                                        if (reserved.containsKey(symbol)) {
                                            throw new MakingException("Cannot use " + markup.code(symbol) + " because " + reserved.get(symbol).why() + "; see /reserved");
                                        }
                                        identifier = ud.newIdentifier(symbol);
                                    }
                                    stack.push(identifier);
                                    debug.push(identifier.getName());
                                } else {
                                    arity = op.getArity();
                                    Expression[] args = new Expression[arity];
                                    if (arity > 0) {
                                        StringBuilder builder = new StringBuilder();
                                        for (int i = 0; i < arity; i++) {
                                            args[arity - 1 - i] = stack.pop();
                                            if (i > 0)
                                                builder.insert(0, ", ");
                                            builder.insert(0, debug.pop());
                                        }
                                        debug.push(symbol + "(" + builder + ")");
                                    } else {
                                        debug.push(symbol);
                                    }
                                    stack.push(op.makeExpression(args));
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
            switch (stack.size()) {
                case 0:
                    reports.add("No expressions were made");
                    break;
                case 1:
                    figureParts.add(new FigurePartImpl(stack.pop(), debug.pop(), color, stroke));
                    break;
                default:
                    boolean undefined = false;
                    StringBuilder builder = new StringBuilder("Multiple expressions were made:");
                    while (!stack.isEmpty()) {
                        Expression extra = stack.pop();
                        String extraString = debug.pop();
                        builder.append("\n").append(markup.code(extraString));
                        if (extra instanceof Identifier) {
                            if (Double.isNaN(((Identifier) extra).getValue())) {
                                builder.append(" (undefined)");
                                undefined = true;
                            }
                        }
                    }
                    if (undefined) {
                        builder.append("\n\nTry /builtin or /userdefined to see a list of defined names");
                    } else {
                        builder.append("\n\nUse ").append(markup.code("and")).append(" between your expressions");
                    }
                    throw new MakingException(builder.toString());
            }
            return new FigureImpl(figureParts, reports.toString(), viewport);
        } else {
            for (Set<Mishap> set : mishaps.values()) {
                for (Mishap mishap : set) {
                    reports.add(mishap.getReport());
                }
            }
            throw new MakingException(reports.toString());
        }
    }

    public enum ReservationReason {
        KEYWORD, COLOR, STROKE, CONSTANT, OPERATOR;

        public String type() {
            switch (this) {
                case KEYWORD:
                    return "Keyword";
                case COLOR:
                    return "Color";
                case STROKE:
                    return "Width";
                case CONSTANT:
                    return "Constant";
                case OPERATOR:
                    return "Operator";
                default:
                    return "Unknown";
            }
        }

        public String why() {
            switch (this) {
                case KEYWORD:
                    return "it is a keyword";
                case COLOR:
                    return "it specifies a certain color";
                case STROKE:
                    return "it specifies a certain width";
                case CONSTANT:
                    return "it refers to a certain constant";
                case OPERATOR:
                    return "it refers to a certain operator";
                default:
                    return "it is reserved";
            }
        }
    }
}
