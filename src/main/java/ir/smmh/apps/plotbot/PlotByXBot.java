package ir.smmh.apps.plotbot;

import annotations.Singleton;
import ir.smmh.apps.plotbot.impl.FigureMakerImpl;
import ir.smmh.apps.plotbot.impl.ViewportImpl;
import ir.smmh.lingu.Maker;
import ir.smmh.lingu.impl.CodeImpl;
import ir.smmh.tgbot.TelegramBotTokens;
import ir.smmh.tgbot.impl.UserManagingTelegramBotImpl;
import ir.smmh.util.GraphicsUtil;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.StringJoiner;

import static ir.smmh.apps.plotbot.MarkupWriter.CachedMessage.*;
import static ir.smmh.util.FunctionalUtil.*;

@Singleton
public class PlotByXBot extends UserManagingTelegramBotImpl<PlotByXBot.UserData> {

    private static final double AREA = Math.pow(2, 20);
    private static final double MIN_SCALE = 4;
    private static PlotByXBot instance;
    private final MarkupWriter markup = MarkupWriter.getInstance();
    private final FigureMaker maker = new FigureMakerImpl();
    private final Color backColor = Color.WHITE;
    private final Color gridColor = Color.BLACK;
    private final Stroke gridStroke = new BasicStroke(1);
    private final Styles styles = Styles.getInstance();
    public Viewport currentViewport = ViewportImpl.DEFAULT;

    private PlotByXBot() {
        super(MarkupWriter.getInstance().getParseMode());
    }

    public static void main(String[] args) {
        PlotByXBot.getInstance().start(TelegramBotTokens.PlotByXBot);
    }

    public static PlotByXBot getInstance() {
        return instance == null ? (instance = new PlotByXBot()) : instance;
    }

    public FigureMaker getMaker() {
        return maker;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void process(long chatId, String text, int messageId) {
        text = text.trim().toLowerCase(Locale.ROOT);
        if (text.charAt(0) == '/') {
            String[] args = text.split(" +");
            if (args.length > 0) {
                String message;
                String command = args[0];
                try {
                    switch (command) {
                        case "/builtin":
                            message = markup.createDocument()
                                    .sectionBegin("Built-in Operators")
                                    .writeList(markup.createList(false)
                                            .appendAllData(sort(convert(maker.getBuiltinOperators(), symbol -> symbol.equals("x") ? null : with(maker.getBuiltinOperator(symbol), operator -> (markup.bold(operator.getType()) + " " + markup.code(symbol)), null)))))
                                    .sectionEnd()
                                    .sectionBegin("Built-in Constants")
                                    .writeList(markup.createList(false)
                                            .appendAllData(sort(convert(maker.getConstants(), name -> with(maker.getConstantValue(name), value -> markup.code(name) + " " + markup.italic("(=" + value + ")"), null)))))
                                    .sectionEnd()
                                    .build().getData();
                            break;
                        //noinspection SpellCheckingInspection
                        case "/userdefined":
                            message = markup.createDocument()
                                    .sectionBegin("User-defined Operators")
                                    .writeList(markup.createList(false)
                                            .appendAllData(sort(convert(maker.getUserDefinedOperators(), symbol -> symbol.equals("x") ? null : with(maker.getUserDefinedOperator(symbol), operator -> (markup.bold(operator.getType()) + " " + markup.code(symbol)), null)))))
                                    .sectionEnd()
                                    .sectionBegin("Variables")
                                    .writeList(markup.createList(false)
                                            .appendAllData(sort(convert(maker.getVariables(), name -> with(maker.getVariableValue(name), value -> markup.code(name) + " " + markup.italic("(=" + value + ")"), null)))))
                                    .sectionEnd()
                                    .build().getData();
                            break;
                        case "/reserved":
                            message = markup.getCachedMessage(RESERVED);
                            break;
                        case "/start":
                            message = markup.getCachedMessage(START);
                            break;
                        case "/help":
                            message = markup.getCachedMessage(HELP);
                            break;
                        case "/commands":
                            message = markup.getCachedMessage(COMMANDS);
                            break;
                        case "/viewport":
                            if (args.length == 1) {
                                currentViewport = ViewportImpl.DEFAULT;
                                message = "Viewport is reset back to: " + markup.code(currentViewport.toString());
                            } else {
                                try {
                                    currentViewport = ViewportImpl.make(
                                            Double.parseDouble(args[1]),
                                            Double.parseDouble(args[2]),
                                            Double.parseDouble(args[3]),
                                            Double.parseDouble(args[4]));
                                    message = "Viewport is set to: " + markup.code(currentViewport.toString());
                                } catch (NumberFormatException e) {
                                    message = "Please provide valid numbers; viewport did not change";
                                } catch (Maker.MakingException e) {
                                    message = e.getMessage();
                                }
                            }
                            break;
                        case "forget":
                            message = markup.code(args[1]).getData() + (maker.forgetUserDefined(args[1]) ? " was forgotten" : " did not exist");
                            break;
                        case "forgetall":
                            message = maker.forgetAllUserDefined() + " user-defined name(s) were forgotten";
                            break;
                        default:
                            message = "Unknown command " + markup.code(command) + "; use /commands to see a list of all available commands.";
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    message = "Not enough arguments for command: " + markup.code(command);
                }
                if (message != null)
                    sendMessage(chatId, message, messageId);
            }
        } else {
            StringJoiner caption = new StringJoiner("\n");
            String hash = Integer.toHexString(Integer.toString(text.hashCode()).hashCode());
//            caption.add("Hash: " + markup.code(hash));
            Figure figure;
            try {
                figure = maker.makeFromCode(new CodeImpl(text, "plt"));
            } catch (Throwable e) {
                sendMessage(chatId, e.getMessage(), messageId);
                return;
            }
            caption.add(figure.getReport());
            //noinspection SpellCheckingInspection
            File file = new File("gen/plotbot/" + hash + ".png");
            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                        sendMessage(chatId, "Unexpected error regarding the existence of image file", messageId);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    sendMessage(chatId, "I/O error while creating empty image file", messageId);
                    return;
                }
            }

            // process the viewport
            Viewport vp = figure.getViewport();
            double precision = 1;
            double ratio = vp.getHorizontalRadius() / vp.getVerticalRadius();
            double vReach = Math.sqrt(AREA / ratio);
            double hReach = ratio * vReach;
            double hScale = hReach / vp.getHorizontalRadius();
            double vScale = vReach / vp.getVerticalRadius();
            double vpx = +vp.getOriginX() * hScale;
            double vpy = -vp.getOriginY() * vScale;
            int w = (int) (hReach * 2);
            int h = (int) (vReach * 2);
            double hOffset = hScale * (+vp.getOriginX() % 1);
            double vOffset = vScale * (-vp.getOriginY() % 1);
            double x1 = vpx - hReach - hOffset;
            double y1 = vpy - vReach - vOffset;
            double x2 = x1 + w;
            double y2 = y1 + h;

            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) image.getGraphics();

            // turn on anti-aliasing
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // clear the background
            g.setColor(backColor);
            g.drawRect(0, 0, w, h);

            // draw the grid lines
            g.setColor(GraphicsUtil.changeTransparency(gridColor, 0.1f));
            g.setStroke(gridStroke);

            if (hScale >= MIN_SCALE)
                for (int x = (int) hOffset; x <= w; x += hScale)
                    g.drawLine(x, 0, x, h);

            if (vScale >= MIN_SCALE)
                for (int y = (int) vOffset; y <= h; y += vScale)
                    g.drawLine(0, y, w, y);

            // draw the parts
            for (Figure.Part part : figure.getParts()) {
                Expression expression = part.getExpression();
                try {
                    expression.evaluate(0);
                } catch (Throwable throwable) {
                    String message = throwable.getMessage();
                    sendMessage(chatId, message == null ? throwable.toString() : message, messageId);
                    return;
                }
                expression.evaluate(Double.NaN);
                g.setColor(styles.getColor(with(part.getColor(), "black")));
                g.setStroke(styles.getStroke(with(part.getStroke(), "thin")));
                double x, y, xPrev = 0, yPrev = 0;
                boolean v, vPrev = true; // is within view
                for (x = x1 - 1; x <= x2; x += precision) {
                    y = -expression.evaluate(x / hScale) * vScale;
                    v = y >= y1 && y <= y2;
                    if (x >= x1) // ignore the first iteration because prevs do not have meaningful values
                        if (v || vPrev) // if either point of the line is within view, draw it
                            g.drawLine(
                                    (int) (x + hReach + hOffset),
                                    (int) (y + vReach + vOffset),
                                    (int) (xPrev + hReach + hOffset),
                                    (int) (yPrev + vReach + vOffset));
                    xPrev = x;
                    yPrev = y;
                    vPrev = v;
                }
                String title = part.getTitle();
                if (title != null)
                    caption.add(Styles.getInstance().getColorEmoji(with(part.getColor(), "black")) + " " + markup.code(title));
            }
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                sendMessage(chatId, "I/O error while writing image file", messageId);
                return;
            }
            try {
                sendPhoto(chatId, file, caption.toString(), messageId);
            } catch (FileNotFoundException e) {
                sendMessage(chatId, "Image file not found", messageId);
            }
        }
    }

    @Override
    public @NotNull PlotByXBot.UserData createUser(long chatId) {
        return new UserData(chatId);
    }

    public static class UserData extends UserManagingTelegramBotImpl.UserData {
        public UserData(long chatId) {
            super(chatId);
        }
    }

}
