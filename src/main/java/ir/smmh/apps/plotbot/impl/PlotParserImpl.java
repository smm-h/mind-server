package ir.smmh.apps.plotbot.impl;

import ir.smmh.apps.plotbot.Expression;
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
    private final Set<String> reserved = new HashSet<>();

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
        defineBuiltinOperator("add", (BinaryOperator) (lhs, rhs) -> (x -> lhs.evaluate(x) + rhs.evaluate(x)));
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
                            literalValue = Double.parseDouble(token.getData());
                            stack.push(x -> literalValue);
                        } catch (NumberFormatException e) {
                            throw new MakingException("could not parse number: <code>" + token.getData() + "</code>");
                        }
                        break;
                    case "id":
                        int arity = 0;
                        String symbol = token.getData();
                        try {
                            if (symbol.equals("assign")) {
                                arity = 2;
                                Expression identifier = stack.pop();
                                if (!(identifier instanceof Identifier)) {
                                    throw new MakingException("cannot assign value to variable using non-identifier expression");
                                }
                                ((Identifier) identifier).value = stack.pop().evaluate(0);
                            } else if (symbol.equals("define")) {
                                arity = 2;
                                Expression identifier = stack.pop();
                                if (!(identifier instanceof Identifier)) {
                                    throw new MakingException("cannot define named function using non-identifier expression");
                                }
                                final int argCount = (int) stack.pop().evaluate(0);
                                Identifier[] boundVariables = new Identifier[argCount];
                                for (int i = 0; i < argCount; i++) {
                                    Expression argVarExpression = stack.pop();
                                    if (argVarExpression instanceof Identifier) {
                                        Identifier argIdentifier = (Identifier) argVarExpression;
                                        if (argIdentifier.value == null) {
                                            identifiers.removeAtPlace(argIdentifier.name); // make it a bound variable
                                            boundVariables[i] = argIdentifier;
                                        } else {
                                            throw new MakingException("cannot use free identifier as bound: <code>" + argIdentifier.name + "</code>");
                                        }
                                    } else {
                                        throw new MakingException("expression must be an identifier");
                                    }
                                }
                                Expression expression = stack.pop();
                                defineOperator(((Identifier) identifier).name, new Operator() {
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
                                        v = new Identifier(symbol, null);
                                    }
                                    stack.push(v);
                                } else {
                                    arity = op.getArity();
                                    Expression[] args = new Expression[op.getArity()];
                                    for (int i = 0; i < args.length; i++) {
                                        Expression intermediate;
                                        intermediate = stack.pop();
                                        args[i] = intermediate;
                                    }
                                    stack.push(op.makeExpression(args));
                                }
                            }
                        } catch (EmptyStackException e) {
                            throw new MakingException("not enough arguments for operation: <code>" + symbol + "</code>, needs " + arity);
                        }
                        break;
                    default:
                        throw new MakingException("unhandled token type: <code>" + token.getTypeString() + "</code>");
                }
            }
            if (stack.isEmpty()) {
                throw new MakingException("no expression made");
            }
            Expression made = stack.pop();
            if (!stack.isEmpty()) {
                StringBuilder builder = new StringBuilder(String.format("extra expressions leftover (%d)", stack.size()));
                while (!stack.isEmpty()) {
                    try {
                        stack.pop().evaluate(0);
                    } catch (NullPointerException e) {
                        builder.append("\n").append(e.getMessage());
                    }
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
        @Nullable Double value;

        private Identifier(String name, @Nullable Double value) throws MakingException {
            if (reserved.contains(name))
                throw new MakingException("cannot reuse reserved identifier: <code>" + name + "</code>");
            identifiers.setAtPlace(name, this);
            this.name = name;
            this.value = value;
        }

        @Override
        public double evaluate(double x) {
            if (value == null) throw new NullPointerException("variable is not assigned: <code>" + name + "</code>");
            return value;
        }
    }
}
