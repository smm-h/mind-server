package ir.smmh.apps.plotbot;

import ir.smmh.tgbot.TelegramBotHTMLWriter;

public class MarkupWriter extends TelegramBotHTMLWriter {

    private static MarkupWriter instance;
    public final String helpMessage = "\n" +
            bold("Welcome to the ") + bold(italic("PlotByX")) + bold(" bot!") + "\n" +
            "\n" +
            "In this simple bot, you can type in expressions and get their plots in return.\n" +
            "\n" +
            "\n" +
            bold("Expressions and operations") + "\n" +
            "\n" +
            "Expressions are written in " + link("post-fix notation", "https://en.wikipedia.org/wiki/Reverse_Polish_notation") + " and consist entirely of numbers and words, no special characters. Words denote operations and variables.\n" +
            "\n" +
            "Simple examples include:\n" +
            "- " + code("x") + "\n" +
            "- " + code("x 2 pow") + "\n" +
            "- " + code("2 x pow") + "\n" +
            "- " + code("x abs") + "\n" +
            "\n" +
            "You can /ops to see the full list of built-in operations, as well as user-defined ones!\n" +
            "\n" +
            "\n" +
            bold("User-defined operations") + "\n" +
            "\n" +
            "You can define your own operations using the following syntax:\n" +
            "\n" +
            "An expression that includes your bound variables, then those variables themselves, then an integer indicating their count, then a name for the function, and then define.\n" +
            "\n" +
            "For example:\n" +
            "- Unary: " + code("a a mul a 1 sqr define") + "\n" +
            "- Binary: " + code("a sqr b sqr sqrt a b 2 hypotenuse define") + "\n" +
            "- Constant: " + code("0.5 0 half define") + "\n" +
            "\n" +
            "Make sure to not use names of free variables or reserved words for the names of your bound variables.\n" +
            "\n" +
            "\n" +
            bold("Free and bound variables") + "\n" +
            "\n" +
            "You can use the " + code("assign") + " operation to assign a value to a variable.\n" +
            "\n" +
            "For example:\n" +
            "- " + code("24 age assign") + "\n" +
            "- " + code("weight height sqr div assign") + "\n" +
            "\n" +
            "If you do this, you will be able to use these variables in your expressions, but you will not be able to use them as bound variables in operation definitions.\n" +
            "\n" +
            "\n" +
            bold("Commands") + "\n" +
            "\n" +
            "In addition to expressions, you can use /commands to interact with the bot.\n";

    private MarkupWriter() {
    }

    public static MarkupWriter getInstance() {
        return instance == null ? (instance = new MarkupWriter()) : instance;
    }
}
