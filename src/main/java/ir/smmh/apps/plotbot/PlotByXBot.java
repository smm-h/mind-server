package ir.smmh.apps.plotbot;

import annotations.Singleton;
import ir.smmh.apps.plotbot.impl.FigureMakerImpl;
import ir.smmh.apps.plotbot.impl.UserDataImpl;
import ir.smmh.apps.plotbot.impl.ViewportImpl;
import ir.smmh.lingu.Maker;
import ir.smmh.tgbot.impl.UserManagingTelegramBotImpl;
import ir.smmh.tgbot.types.ChatMember;
import ir.smmh.tgbot.types.Update;
import ir.smmh.tgbot.types.impl.InlineQueryResultImpl;
import ir.smmh.util.GraphicsUtil;
import ir.smmh.util.RandomUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sensitive.TelegramBotTokens;
import sensitive.TelegramUniqueIds;

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
public class PlotByXBot extends UserManagingTelegramBotImpl<UserData> {

    private static final double MIN_SCALE = 4;
    private static PlotByXBot instance;
    private final MarkupWriter markup = MarkupWriter.getInstance();
    private final FigureMaker maker = new FigureMakerImpl();
    private final Styles styles = Styles.getInstance();
    private final Color gridColor = GraphicsUtil.changeTransparency(Color.GRAY, 0.3f);
    private final Color axesColor = GraphicsUtil.changeTransparency(Color.GRAY, 0.6f);
    private final Stroke gridStroke = new BasicStroke(1);
    private final Stroke axesStroke = new BasicStroke(1);
    public Viewport currentViewport = ViewportImpl.DEFAULT;
    private long cacheChat = 0;

    private PlotByXBot() {
        super(MarkupWriter.getInstance().getParseMode());
        addHandler((Update.Handler.message) message -> {
            String text = message.text();
            if (text == null) return;
            long chatId = message.chat().id();
            int messageId = message.message_id();
            handle(text, chatId, messageId);
        });
        addHandler((Update.Handler.inline_query) inlineQuery -> {
            if (cacheChat != 0) {
                String text = inlineQuery.query();
                String photo_file_id = null;
                handle(text, cacheChat, null);
                answerInlineQuery(inlineQuery.id(), new InlineQueryResultImpl.CachedPhoto(
                        RandomUtil.generateRandomHex(16), photo_file_id, null, null, null
                ));
            }
        });
        addHandler((Update.Handler.my_chat_member) chatMemberUpdated -> {
            if (chatMemberUpdated.from().id() == TelegramUniqueIds.ADMIN
                    && chatMemberUpdated.chat().id() == TelegramUniqueIds.CACHE) {
                ChatMember cm = chatMemberUpdated.new_chat_member();
                if (cm instanceof ChatMember.Administrator) {
                    ChatMember.Administrator admin = (ChatMember.Administrator) cm;
                    if (admin.can_post_messages()) {
                        cacheChat = TelegramUniqueIds.CACHE;
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        PlotByXBot.getInstance().start(TelegramBotTokens.PlotByXBot);
    }

    public static PlotByXBot getInstance() {
        return instance == null ? (instance = new PlotByXBot()) : instance;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void handle(String text, long chatId, @Nullable Integer messageId) {
        System.out.println("@" + chatId + " " + (messageId == null ? "INLINE" : "#" + messageId) + ": " + text);
        UserData ud = getUser(chatId);
        text = text.trim().toLowerCase(Locale.ROOT);
        if (text.charAt(0) == '/') {
            String[] args = text.split(" +");
            if (args.length > 0) {
                String output;
                String command = args[0];
                try {
                    switch (command) {
                        case "/builtin":
                            output = markup.createDocument()
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
                        case "/userdefined":
                            output = markup.createDocument()
                                    .sectionBegin("User-defined Operators")
                                    .writeList(markup.createList(false)
                                            .appendAllData(sort(convert(ud.getUserDefinedOperators(), symbol -> symbol.equals("x") ? null : with(ud.getUserDefinedOperator(symbol), operator -> (markup.bold(operator.getType()) + " " + markup.code(symbol)), null)))))
                                    .sectionEnd()
                                    .sectionBegin("Variables")
                                    .writeList(markup.createList(false)
                                            .appendAllData(sort(convert(ud.getVariables(), name -> with(ud.getVariableValue(name), value -> markup.code(name) + " " + markup.italic("(=" + value + ")"), null)))))
                                    .sectionEnd()
                                    .build().getData();
                            break;
                        case "/reserved":
                            output = markup.getCachedMessage(RESERVED);
                            break;
                        case "/start":
                            output = markup.getCachedMessage(START);
                            break;
                        case "/help":
                            output = markup.getCachedMessage(HELP);
                            break;
                        case "/commands":
                            output = markup.getCachedMessage(COMMANDS);
                            break;
                        case "/viewport":
                            if (args.length == 1) {
                                currentViewport = ViewportImpl.DEFAULT;
                                output = "Viewport is reset back to: " + markup.code(currentViewport.toString());
                            } else {
                                try {
                                    currentViewport = ViewportImpl.make(
                                            Double.parseDouble(args[1]),
                                            Double.parseDouble(args[2]),
                                            Double.parseDouble(args[3]),
                                            Double.parseDouble(args[4]));
                                    output = "Viewport is set to: " + markup.code(currentViewport.toString());
                                } catch (NumberFormatException e) {
                                    output = "Please provide valid numbers; viewport did not change";
                                } catch (Maker.MakingException e) {
                                    output = e.getMessage();
                                }
                            }
                            break;
                        case "/forget":
                            output = markup.code(args[1]).getData() + (ud.forgetUserDefined(args[1]) ? " was forgotten" : " did not exist");
                            break;
                        case "/forgetall":
                            output = ud.forgetAllUserDefined() + " user-defined name(s) were forgotten";
                            break;
                        case "/toggledarkmode":
                            output = "Dark mode is now " + (ud.toggleDarkMode() ? "on \uD83C\uDF1A" : "off \uD83C\uDF1D");
                            break;
                        default:
                            output = "Unknown command " + markup.code(command) + "; use /commands to see a list of all available commands.";
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    output = "Not enough arguments for command: " + markup.code(command);
                }
                if (output != null)
                    sendMessage(chatId, output, messageId);
            }
        } else {
            StringJoiner caption = new StringJoiner("\n");
            String hash = Integer.toHexString(Integer.toString(text.hashCode()).hashCode());
//            caption.add("Hash: " + markup.code(hash));
            Figure figure;
            try {
                figure = maker.make(chatId, text);
            } catch (Throwable e) {
                sendMessage(chatId, e.getMessage(), messageId);
                return;
            }
            if (figure.getParts().isEmpty()) {
                sendMessage(chatId, figure.getReport(), messageId);
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

            // use the viewport to make the image
            Viewport vp = figure.getViewport();
            int w = vp.getWidth();
            int h = vp.getHeight();
            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) image.getGraphics();

            // turn on anti-aliasing
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean darkMode = ud.isInDarkMode();

            // clear the background
            g.setColor(darkMode ? Color.BLACK : Color.WHITE);
            g.fillRect(0, 0, w, h);

            // draw the grid lines
            g.setColor(gridColor);
            g.setStroke(gridStroke);

            g.setFont(new Font("Consolas", Font.PLAIN, 72));

            if (vp.getHorizontalScale() >= MIN_SCALE)
                for (double x = Math.floor(vp.getCenterX() - vp.getHorizontalRadius()); x <= vp.getHorizontalRadius(); x += 1) {
                    int px = vp.getPixelX(x);
                    g.drawLine(px, 0, px, h);
                }

            if (vp.getVerticalScale() >= MIN_SCALE)
                for (double y = Math.floor(vp.getCenterY() - vp.getVerticalRadius()); y <= vp.getVerticalRadius(); y += 1) {
                    int py = vp.getPixelY(y);
                    g.drawLine(0, py, w, py);
                }

            // draw the axes
            g.setColor(axesColor);
            g.setStroke(axesStroke);
            int x0 = vp.getPixelX(0);
            int y0 = vp.getPixelY(0);
            g.drawLine(x0, 0, x0, h);
            g.drawLine(0, y0, w, y0);

            // draw the parts
            for (Figure.Part part : figure.getParts()) {
                Expression expression = part.getExpression();
                try {
                    expression.evaluate(0);
                } catch (Throwable throwable) {
                    String output = throwable.getMessage();
                    sendMessage(chatId, output == null ? throwable.toString() : output, messageId);
                    return;
                }
                expression.evaluate(Double.NaN);
                g.setColor(styles.getColor(with(part.getColor(), darkMode ? "white" : "black")));
                g.setStroke(styles.getStroke(with(part.getStroke(), "thin")));
                int extra = (int) Math.ceil(((BasicStroke) g.getStroke()).getLineWidth());
                int x, y, xPrev = 0, yPrev = 0;
                boolean v, vPrev = true; // is within view
                for (x = -extra; x <= w + extra; x += 1) {
                    y = vp.getPixelY(expression.evaluate(vp.getRealX(x)));
                    v = y >= -extra && y <= h + extra;
                    if (x > -extra) // ignore the first iteration because prevs do not have meaningful values
                        if (v || vPrev) // if either point of the line is within view, draw it
                            g.drawLine(x, y, xPrev, yPrev);
                    xPrev = x;
                    yPrev = y;
                    vPrev = v;
                }
                String title = part.getTitle();
                if (title != null)
                    caption.add(Styles.getInstance().getColorEmoji(with(part.getColor(), darkMode ? "white" : "black")) + " " + markup.code(title));
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

    public FigureMaker getMaker() {
        return maker;
    }

    @Override
    public @NotNull UserDataImpl createUser(long chatId) {
        return new UserDataImpl(chatId);
    }

}
