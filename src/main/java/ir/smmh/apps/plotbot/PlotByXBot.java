package ir.smmh.apps.plotbot;

import annotations.Singleton;
import ir.smmh.apps.plotbot.impl.PlotParserImpl;
import ir.smmh.lingu.Maker;
import ir.smmh.tgbot.TelegramBotTokens;
import ir.smmh.tgbot.impl.UserManagingTelegramBotImpl;
import ir.smmh.util.GraphicsUtil;
import ir.smmh.util.jile.Chronometer;
import ir.smmh.util.jile.impl.NChronometer;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

@Singleton
public class PlotByXBot extends UserManagingTelegramBotImpl<PlotByXBot.UserData> {

    private final PlotParser parser = new PlotParserImpl();
    private final Color colorFore = Color.BLACK;
    private final Color colorBack = Color.WHITE;
    private final Stroke stroke = new BasicStroke(3);
    private final double timeOut = 3000;
    private final MarkupWriter markup = MarkupWriter.getInstance();

    public PlotByXBot() {
        super(MarkupWriter.getInstance().getParseMode());
    }

    public static void main(String[] args) {
        new PlotByXBot().start(TelegramBotTokens.PlotByXBot);
    }

    @Override
    public void process(long chatId, String text, int messageId) {
        if (text.charAt(0) == '/') {
            String[] args = text.split(" ");
            if (args.length > 0) {
                String command = args[0];
                try {
                    switch (command) {
                        case "/ops":
                            List<String> list = new ArrayList<>();
                            StringJoiner joiner = new StringJoiner("\n", "Available operators are:\n\n", "");
                            for (String symbol : parser.getBuiltinOperators()) {
                                if (symbol.equals("x")) continue;
                                Operator operator = parser.getBuiltinOperator(symbol);
                                if (operator == null) continue;
                                Double value = null;
                                if (operator.getArity() == 0 && !symbol.equals("random")) {
                                    try {
                                        value = operator.makeExpression().evaluate(0);
                                    } catch (Maker.MakingException ignored) {
                                    }
                                }
                                list.add(markup.bold(operator.getType()) + " " + markup.code(symbol) + (value == null ? "" : " " + markup.italic(String.format("(=%f)", value))));
                            }
                            Collections.sort(list);
                            for (String i : list) {
                                joiner.add(i);
                            }
                            sendMessage(chatId, joiner.toString(), messageId);
                            break;
                        case "/help":
                            sendMessage(chatId, markup.helpMessage, messageId);
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    sendMessage(chatId, "not enough arguments for command: " + markup.code(command), messageId);
                }
            }
        } else {
            Expression expression;
            try {
                expression = parser.parse(text);
            } catch (Maker.MakingException e) {
                sendMessage(chatId, e.getMessage(), messageId);
                return;
            }
            try {
                expression.evaluate(0);
            } catch (Throwable e) {
                sendMessage(chatId, e.getMessage(), messageId);
                return;
            }
            File file = new File("gen/plotbot/" + text.hashCode() + ".png");
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
                int w = 256;
                int h = 256;
                int hScale = w / 8;
                int vScale = h / 8;
                int hTicks = 1;
                int vTicks = 1;
                BufferedImage image = new BufferedImage(w * 2, h * 2, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) image.getGraphics();
                g.setColor(colorBack);
                g.drawRect(0, 0, w * 2, h * 2);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(GraphicsUtil.changeTransparency(colorFore, 0.1f));
                g.setStroke(stroke);
                for (int x = 0; x <= w; x += hScale * hTicks) {
                    g.drawLine(w + x, 0, w + x, h * 2);
                    g.drawLine(w - x, 0, w - x, h * 2);
                }
                for (int y = 0; y <= h; y += vScale * vTicks) {
                    g.drawLine(0, h + y, w * 2, h + y);
                    g.drawLine(0, h - y, w * 2, h - y);
                }
                g.setColor(colorFore);
                Chronometer c = new NChronometer();
                double totalElapsedTime = 0;
                int iterationCount = 0;
                double x, y, x_prev = 0, y_prev = 0;
                for (x = -w; x <= w; x++) {
                    c.reset();
                    y = -expression.evaluate(x / hScale) * vScale;
                    if (x > -w)
                        g.drawLine((int) x + w, (int) y + h, (int) x_prev + w, (int) y_prev + h);
                    x_prev = x;
                    y_prev = y;
                    totalElapsedTime += c.stop();
                    iterationCount++;
                    if (totalElapsedTime >= timeOut) {
                        sendMessage(chatId, "Timed out", messageId);
                        break;
                    }
                }
                if (iterationCount > 10) {
                    sendMessage(chatId, String.format("This took about %fms per calculation", totalElapsedTime / iterationCount), messageId);
                } else {
                    sendMessage(chatId, "This took way too long to calculate", messageId);
                }
                try {
                    ImageIO.write(image, "png", file);
                } catch (IOException e) {
                    sendMessage(chatId, "I/O error while writing image file", messageId);
                    return;
                }
            }
            try {
                sendPhoto(chatId, file, markup.code(text).getData(), messageId);
            } catch (FileNotFoundException e) {
                sendMessage(chatId, "Image file not found", messageId);
            }
        }
    }

    @Override
    public @NotNull PlotByXBot.UserData createUser(long chatId) {
        return null;
    }

    public static class UserData extends UserManagingTelegramBotImpl.UserData {
        public UserData(long chatId) {
            super(chatId);
        }
    }

}
