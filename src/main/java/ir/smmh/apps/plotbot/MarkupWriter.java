package ir.smmh.apps.plotbot;

import ir.smmh.tgbot.TelegramBotHTMLWriter;

import java.util.HashMap;
import java.util.Map;

import static ir.smmh.util.FunctionalUtil.*;

public class MarkupWriter extends TelegramBotHTMLWriter {

    private static MarkupWriter instance;
    private final Map<CachedMessage, String> cache = new HashMap<>();

    private MarkupWriter() {
    }

    public static MarkupWriter getInstance() {
        return instance == null ? (instance = new MarkupWriter()) : instance;
    }

    public String getCachedMessage(CachedMessage command) {
        if (!cache.containsKey(command)) {
            cache.put(command, generateMessage(command));
        }
        return cache.get(command);
    }

    private String generateMessage(CachedMessage message) {
        FigureMaker maker = PlotByXBot.getInstance().getMaker();
        switch (message) {
            case START:
                return createDocument()
                        .sectionBegin("Welcome to " + italic("PlotByX") + " bot!")
                        .writeParagraph("You can type in expressions and get their plots in return. For example try sending the bot a simple 'x' and see what happens!")
                        .writeParagraph("Use the /help command to get started.")
                        .build().getData();
            case HELP:
                return createDocument()
                        .sectionBegin("Getting Started")
                        .writeParagraph("In this simple bot, you can type in expressions and get their plots in return.")
                        .sectionBegin("Expressions and operations")
                        .writeParagraph("Expressions are written in " + link("post-fix notation", "https://en.wikipedia.org/wiki/Reverse_Polish_notation") + " and consist entirely of numbers and words, no special characters. Words denote operations and variables.")
                        .writeList(createList(false)
                                        .append(code("x"))
                                        .append(code("x 2 pow"))
                                        .append(code("2 x pow"))
                                        .append(code("x abs"))
                                , "Simple examples include:")
                        .sectionEnd()
                        .writeParagraph("You can /builtin to see the full list of built-in operations, or /userDefined for user-defined ones!")
                        .sectionBegin("User-defined operations")
                        .writeParagraph("You can define your own operations using the following syntax:")
                        .writeParagraph("An expression that includes your bound variables, then those variables themselves, then an integer indicating their count, then a name for the function, and then define.")
                        .writeList(createList(false)
                                        .appendData("Unary: " + code("a a mul a 1 sqr define"))
                                        .appendData("Binary: " + code("a sqr b sqr sqrt a b 2 hypotenuse define"))
                                        .appendData("Nullary: " + code("0.5 0 half define"))
                                , "For example:")
                        .writeParagraph("Make sure to not use names of free variables or reserved words for the names of your bound variables.")
                        .sectionEnd()
                        .sectionBegin("Free and bound variables")
                        .writeParagraph("You can use the " + code("assign") + " operation to assign a value to a variable.")
                        .writeList(createList(false)
                                        .append(code("24 age assign"))
                                        .append(code("weight height sqr div assign"))
                                , "For example:")
                        .writeParagraph("If you do this, you will be able to use these variables in your expressions, but you will not be able to use them as bound variables in operation definitions.")
                        .sectionEnd()
                        .sectionBegin("Commands")
                        .writeParagraph("In addition to expressions, you can use /commands to interact with the bot.")
                        .sectionEnd()
                        .sectionEnd()
                        .build().getData();
            case COMMANDS:
                return createDocument()
                        .sectionBegin("Commands")
                        .writeList(createList(false)
                                .appendData("/start Show the start message")
                                .appendData("/help Show the help message")
                                .appendData("/commands Show this message")
                                .appendData("/builtin Show a list of all built-in operators and constants")
                                .appendData("/userDefined Show a list of all user-defined operators and variables")
                                .appendData("/reserved Show a list of all reserved names")
                                .appendData("/viewport " + code("x") + " " + code("y") + " " + code("hr") + " " + code("vr") + " Change the current viewport; use without arguments to reset to default")
                                .appendData("/forget " + code("name") + " Forget a user-defined operator or variable")
                                .appendData("/forgetAll Forget all user-defined operators and variables")
                                .appendData("/toggleDarkMode Toggle dark-mode friendly color scheme")
                        )
                        .sectionEnd()
                        .build().getData();
            case RESERVED:
                return createDocument()
                        .sectionBegin("Reserved Names")
                        .writeList(createList(false)
                                .appendAllData(sort(convert(maker.getReservedNames(), name -> with(maker.getReservedReasonType(name), rt -> rt + " " + code(name), null)))))
                        .sectionEnd()
                        .build().getData();
        }
        return "";
    }

    public enum CachedMessage {
        START, HELP, COMMANDS, RESERVED
    }
}
