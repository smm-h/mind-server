package ir.smmh.apps.plotbot;

import ir.smmh.tgbot.TelegramBotHTMLWriter;

public class MarkupWriter extends TelegramBotHTMLWriter {

    private static MarkupWriter instance;
    public final String helpMessage;

    {
        Document d = createDocument();
        d
                .sectionBegin("Welcome to " + italic("PlotByX") + " bot!")
                .writeParagraph("In this simple bot, you can type in expressions and get their plots in return.")
                .sectionBegin("Expressions and operations")
                .writeParagraph("Expressions are written in " + link("post-fix notation", "https://en.wikipedia.org/wiki/Reverse_Polish_notation") + " and consist entirely of numbers and words, no special characters. Words denote operations and variables.")
                .writeList(createList(false)
                                .append(code("x"))
                                .append(code("x 2 pow"))
                                .append(code("2 x pow"))
                                .append(code("x abs"))
                        , "Simple examples include:")
                .writeParagraph("You can /ops to see the full list of built-in operations, as well as user-defined ones!")
                .sectionEnd()
                .sectionBegin("User-defined operations")
                .writeParagraph("You can define your own operations using the following syntax:")
                .writeParagraph("An expression that includes your bound variables, then those variables themselves, then an integer indicating their count, then a name for the function, and then define.")
                .writeList(createList(false)
                                .append("Unary: " + code("a a mul a 1 sqr define"))
                                .append("Binary: " + code("a sqr b sqr sqrt a b 2 hypotenuse define"))
                                .append("Constant: " + code("0.5 0 half define"))
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
        ;
        helpMessage = d.build().getData();
    }

    private MarkupWriter() {
    }

    public static MarkupWriter getInstance() {
        return instance == null ? (instance = new MarkupWriter()) : instance;
    }

    public String expressionRequestLine(Figure.Part r, double cps) {
        String string = "";
        String color = r.getColor();
        if (color != null) {
            string += PlotByXBotStyles.getInstance().getColorEmoji(color) + " ";
        }
        return string + code(r.getTitle()); // + " " + italic(String.format("(%d c/ms)", (int) cps));
    }
}
