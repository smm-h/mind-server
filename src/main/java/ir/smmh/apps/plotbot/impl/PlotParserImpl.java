package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.Expression;
import ir.smmh.apps.plotbot.MarkupWriter;
import ir.smmh.apps.plotbot.Operator;
import ir.smmh.apps.plotbot.PlotParser;
import ir.smmh.lingu.Code;
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

public class PlotParserImpl extends LanguageImpl implements PlotParser {

    private final Map.SingleValue.Mutable<String, Operator> builtinOps = new MapImpl.SingleValue.Mutable<>();
    private final Map.SingleValue.Mutable<String, Operator> userDefinedOps = new MapImpl.SingleValue.Mutable<>();
    private final Map.SingleValue.Mutable<String, Identifier> identifiers = new MapImpl.SingleValue.Mutable<>();
    private final Stack<Expression> stack = new Stack<>();
    private final Stack<String> debug = new Stack<>();
    private final Set<String> reserved = new HashSet<>();
    private final MarkupWriter markup = MarkupWriter.getInstance();

    @SuppressWarnings("SpellCheckingInspection")
    public PlotParserImpl() {
        super("PlotLang", "plt", new MultiprocessorImpl());
        TokenizerImpl tokenizer = new TokenizerImpl();
        tokenizer.define(new TokenizerMakerImpl.Streak("whitespace", " \n\t\r"));
        tokenizer.ignore("whitespace");
        tokenizer.define(new TokenizerMakerImpl.Streak("id", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"));
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
        defineBuiltinOperator("append", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) + rhs.evaluate(x)));
        defineBuiltinOperator("sub", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) - rhs.evaluate(x)));
        defineBuiltinOperator("mul", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) * rhs.evaluate(x)));
        defineBuiltinOperator("div", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) / rhs.evaluate(x)));
        defineBuiltinOperator("mod", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) % rhs.evaluate(x)));
        defineBuiltinOperator("pow", (BinaryOperator) (lhs, rhs) -> (x -> Math.pow(lhs.evaluate(x), rhs.evaluate(x))));
    }

    @Override
    public @NotNull Expression parse(String string) throws MakingException {
        return makeFromCode(new CodeImpl(string, "plt"));
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
    public @NotNull Expression makeFromCode(@NotNull Code code) throws MakingException {
        List<Token.Individual> tokens = TokenizerImpl.tokenized.read(code);
//        System.out.println(((Comprehension.List<Token.Individual, String>) token -> token.getTypeString() + ":" + token.getData()).comprehend(tokens));
        java.util.Map<Token.Individual, Set<Mishap>> mishaps = CodeImpl.mishaps.read(code);
        if (mishaps.isEmpty()) {
            if (tokens == null) throw new MakingException("tokenized port empty");
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
                            throw new MakingException("could not parse number: " + markup.code(token.getData()));
                        }
                        break;
                    case "id":
                        int arity = 0;
                        String symbol = token.getData();
                        try {
                            if (symbol.equals("assign")) {
                                arity = 2;
                                StringBuilder builder = new StringBuilder();
                                builder.append("ASSIGN: ");
                                Expression identifier = stack.pop();
                                debug.pop();
                                if (!(identifier instanceof Identifier)) {
                                    throw new MakingException("cannot assign value to variable using non-identifier expression");
                                }
                                Identifier verifiedIdentifier = (Identifier) identifier;
                                builder.append(verifiedIdentifier.name).append(" = ");
                                double value = stack.pop().evaluate(Double.NaN);
                                builder.append(debug.pop()).append(" = ").append(value);
                                System.out.println(builder);
                                if (Double.isNaN(value))
                                    throw new MakingException("expression evaluation failed");
                                verifiedIdentifier.value = value;
                            } else if (symbol.equals("define")) {
                                arity = 2;
                                StringBuilder builder = new StringBuilder();
                                builder.append("DEFINE: ");
                                Expression identifier = stack.pop();
                                debug.pop();
                                if (!(identifier instanceof Identifier)) {
                                    throw new MakingException("cannot define named function using non-identifier expression");
                                }
                                Identifier verifiedIdentifier = (Identifier) identifier;
                                builder.append(verifiedIdentifier.name);
                                StringJoiner argJoiner = new StringJoiner(", ", "(", ")");
                                final int argCount = (int) stack.pop().evaluate(Double.NaN);
                                debug.pop();
                                Identifier[] boundVariables = new Identifier[argCount];
                                for (int i = 0; i < argCount; i++) {
                                    Expression argIdentifier = stack.pop();
                                    debug.pop();
                                    if (argIdentifier instanceof Identifier) {
                                        Identifier verifiedArgIdentifier = (Identifier) argIdentifier;
                                        argJoiner.add(verifiedArgIdentifier.name);
                                        if (Double.isNaN(verifiedArgIdentifier.value)) {
                                            identifiers.removeAtPlace(verifiedArgIdentifier.name); // make it a bound variable
                                            boundVariables[i] = verifiedArgIdentifier;
                                        } else {
                                            throw new MakingException("cannot use free identifier as bound:" + markup.code(verifiedArgIdentifier.name));
                                        }
                                    } else {
                                        throw new MakingException("expression must be an identifier");
                                    }
                                }
                                builder.append(argJoiner).append(" := ");
                                Expression expression = stack.pop();
                                builder.append(debug.pop()).append(">");
                                System.out.println(builder);
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
                            } else {
                                Operator op = builtinOps.getAtPlace(symbol);
                                if (op == null) {
                                    Identifier v = identifiers.getAtPlace(symbol);
                                    if (v == null) {
                                        v = new Identifier(symbol, Double.NaN);
                                    }
                                    stack.push(v);
                                    debug.push(v.name);
                                } else {
                                    StringJoiner joiner = new StringJoiner(", ", "(", ")");
                                    arity = op.getArity();
                                    Expression[] args = new Expression[op.getArity()];
                                    for (int i = 0; i < args.length; i++) {
                                        Expression intermediate;
                                        intermediate = stack.pop();
                                        joiner.add(debug.pop());
                                        args[i] = intermediate;
                                    }
                                    stack.push(op.makeExpression(args));
                                    debug.push(joiner.toString());
                                }
                            }
                        } catch (EmptyStackException e) {
                            throw new MakingException("not enough arguments for operation: " + markup.code(symbol) + ", needs " + arity);
                        }
                        break;
                    default:
                        throw new MakingException("unhandled token type: " + markup.code(token.getTypeString()) + "");
                }
            }
            if (stack.isEmpty()) {
                throw new MakingException("no expression made");
            }
            Expression made = stack.pop();
            System.out.println("PLOT: " + debug.pop());
            if (!stack.isEmpty()) {
                StringBuilder builder = new StringBuilder("extra expressions leftover:");
                while (!stack.isEmpty()) {
                    stack.pop();
                    builder.append("\n").append(markup.code(debug.pop()));
                }
                throw new MakingException(builder.toString());
            }
            return made;
        } else {
            StringJoiner report = new StringJoiner("\n");
            for (Set<Mishap> set : mishaps.values()) {
                for (Mishap mishap : set) {
                    report.add(mishap.getReport());
                }
            }
            throw new MakingException(report.toString());
        }
    }

    private class Identifier implements Expression {
        final String name;
        double value;

        private Identifier(String name, double value) throws MakingException {
            if (reserved.contains(name))
                throw new MakingException("cannot reuse reserved identifier: " + markup.code(name));
            identifiers.setAtPlace(name, this);
            this.name = name;
            this.value = value;
        }

        @Override
        public double evaluate(double x) {
            if (Double.isNaN(value))
                throw new NullPointerException("variable is not assigned: " + markup.code(name));
            return value;
        }
    }
}
